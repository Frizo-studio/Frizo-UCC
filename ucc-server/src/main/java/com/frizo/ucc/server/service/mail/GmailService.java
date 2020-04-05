package com.frizo.ucc.server.service.mail;

public interface GmailService {
    void sendEmailVerifiyCode(String to, String verifiyCode);
}
