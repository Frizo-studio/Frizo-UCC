package com.frizo.ucc.server.service.following.impl;

import com.frizo.ucc.server.dao.following.FollowingRepository;
import com.frizo.ucc.server.dao.notice.UserNoticeRepository;
import com.frizo.ucc.server.dao.user.UserRepository;
import com.frizo.ucc.server.exception.ResourceNotFoundException;
import com.frizo.ucc.server.model.Following;
import com.frizo.ucc.server.model.FollowingPrimarykey;
import com.frizo.ucc.server.model.User;
import com.frizo.ucc.server.model.UserNotice;
import com.frizo.ucc.server.payload.response.UserNoticeCount;
import com.frizo.ucc.server.payload.response.bean.UserBean;
import com.frizo.ucc.server.service.following.FollowingService;
import com.frizo.ucc.server.service.notice.PushNoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FollowingServiceImpl implements FollowingService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowingRepository followingRepository;

    @Autowired
    UserNoticeRepository userNoticeRepository;

    @Autowired
    PushNoticeService pushNoticeService;

    @Override
    public List<UserBean> findAllMyFollowing(Long userId, boolean accepted) {
        List<Following> followingList = followingRepository.findAllByUserIdAndAcceptedEquals(userId, accepted);
        List <UserBean> beans = new ArrayList<>();
        followingList.forEach(following -> {
            UserBean bean = new UserBean();
            BeanUtils.copyProperties(following.getFollowingUser(), bean);
            beans.add(bean);
        });
        return beans;
    }

    @Override
    public List<UserBean> findAllMyFollowers(Long userId, boolean accepted) {
        List<Following> followerList = followingRepository.findAllByFollowingUserIdAndAcceptedEquals(userId, accepted);
        List <UserBean> beans = new ArrayList<>();
        followerList.forEach(following -> {
            UserBean bean = new UserBean();
            BeanUtils.copyProperties(following.getUser(), bean);
            beans.add(bean);
        });
        return beans;
    }

    @Override
    @Transactional
    public void acceptFollowingRequest(Long requesterId, Long followingUserId) {
        Optional<Following> optional = followingRepository.findById(new FollowingPrimarykey(requesterId, followingUserId));
        Following following = optional.orElseThrow(() -> {
            throw new ResourceNotFoundException("查無追蹤者資料");
        });
        following.setAccepted(true);
        followingRepository.save(following);
    }

    @Override
    @Transactional
    public void createFollowing(Long requesterId, Long followingUserId) {
        User requester = userRepository.getOne(requesterId);
        Optional<User> targetOptional = userRepository.findById(followingUserId);
        targetOptional.ifPresent(targetUser -> {
            Following following = new Following();
            following.setFollowingPrimarykey(new FollowingPrimarykey(requesterId, followingUserId));
            following.setUser(requester);
            following.setFollowingUser(targetUser);
            following.setAccepted(targetUser.isActivelyAcceptFollowRequest());
            followingRepository.save(following);

            if (!following.isAccepted()) { // 建立 Notice
                Optional<UserNotice> targetUserNotice = userNoticeRepository.findById(targetUser.getId());
                UserNotice userNotice;
                if (targetUserNotice.isEmpty()){
                    userNotice = new UserNotice();
                    //userNotice.setId(targetUser.getId());
                    userNotice.setUser(targetUser);
                    userNotice.setChatNoticeCount(0);
                    userNotice.setEventNotiveCount(0);
                    userNotice.setFollowingNoticeCount(1);
                }else{
                    userNotice = targetUserNotice.get();
                    userNotice.setFollowingNoticeCount(userNotice.getFollowingNoticeCount() + 1);
                }

                userNoticeRepository.save(userNotice);

                UserNoticeCount noticeCount = new UserNoticeCount();
                BeanUtils.copyProperties(userNotice, noticeCount);
                pushNoticeService.sendUserNoticeCount(targetUser.getId().toString(), noticeCount);
            }
        });

        if (targetOptional.isEmpty()){
            throw new ResourceNotFoundException("追蹤用戶不存在");
        }
    }



    @Override
    @Transactional
    public void deleteFollowing(Long requesterId, Long followingUserId) {
        followingRepository.deleteById(new FollowingPrimarykey(requesterId, followingUserId));
    }
}
