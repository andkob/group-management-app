package com.melon.app.service;

import java.io.InputStream;

public interface CredentialsProvider {
    InputStream getCredentialsStream();
}
