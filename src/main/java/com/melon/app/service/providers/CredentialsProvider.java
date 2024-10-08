package com.melon.app.service.providers;

import java.io.InputStream;

/**
 * This Interface defines the method for obtaining the input stream for credentials.
 */
public interface CredentialsProvider {
    /**
     * Retrieves an {@link InputStream} for a credentials file.
     *
     * @return an {@code InputStream} of the credentials file, or {@code null} if the file is not found.
     */
    InputStream getCredentialsStream();
}
