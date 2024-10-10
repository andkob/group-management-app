package com.melon.group_scheduler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.melon.app.service.GoogleSheetsService;
import com.melon.app.service.objects.FormUpdateResponse;
import com.melon.app.service.providers.CredentialsProvider;
import com.melon.app.service.providers.DefaultCredentialsProvider;

@ExtendWith(MockitoExtension.class)
public class GoogleSheetsServiceTest {

    @InjectMocks
    private GoogleSheetsService googleSheetsService;

    @Mock
    private HttpTransport httpTransport;

    private static final String SPREADSHEET_ID = "1iXAKutl9do2Nwf0X4RQMR4QDx7unk4h83kSWp1eR8Oo";

    private static final String MOCK_CREDENTIALS = "{\n" +
            "  \"installed\": {\n" +
            "    \"client_id\": \"mock-client-id.apps.googleusercontent.com\",\n" +
            "    \"project_id\": \"mock-project-id\",\n" +
            "    \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "    \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "    \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "    \"client_secret\": \"mock-client-secret\",\n" +
            "    \"redirect_uris\": [\"urn:ietf:wg:oauth:2.0:oob\", \"http://localhost\"]\n" +
            "  }\n" +
            "}";

    @BeforeEach
    void setUp() throws IOException, GeneralSecurityException {
        // MockitoAnnotations.openMocks(this);
        
        // Use reflection to set private static final fields if necessary
        // ReflectionTestUtils.setField(GoogleSheetsService.class, "CREDENTIALS_FILE_PATH", "/credentials.json");

        // Reset to default provider before each test
        GoogleSheetsService.setCredentialsProvider(new DefaultCredentialsProvider());
    }

    @Test
    void testGetCredentials() throws IOException, GeneralSecurityException {
        boolean useMockData = true;
        if (useMockData) {
            // Create a mock credentials provider
            CredentialsProvider mockProvider = new CredentialsProvider() {
                @Override
                public InputStream getCredentialsStream() {
                    return new ByteArrayInputStream(MOCK_CREDENTIALS.getBytes());
                }
            };

            // Set the mock provider
            GoogleSheetsService.setCredentialsProvider(mockProvider);
        }
        

        // Use reflection to invoke the private getCredentials method
        Credential credential = (Credential) ReflectionTestUtils.invokeMethod(
            googleSheetsService, // googleSheetsService
            "getCredentials",
            GoogleNetHttpTransport.newTrustedTransport()
        );

        // prints to debug console
        System.out.println("access token = " + credential.getAccessToken());
        
        // Assert that the credential is not null
        assertNotNull(credential);
    }

    @Test
    void testGetCredentialsFileNotFound() {
        // Create a mock provider that returns null
        CredentialsProvider mockProvider = new CredentialsProvider() {
            @Override
            public InputStream getCredentialsStream() {
                return null;
            }
        };
        
        // Set the mock provider
        GoogleSheetsService.setCredentialsProvider(mockProvider);
        
        // Test that service creation throws FileNotFoundException
        assertThrows(FileNotFoundException.class, () -> {
            new GoogleSheetsService();
        });
    }

    /**
     * NOTE: This test will pass as long as a verified test user is accessing a
     * form they have read permissions for.
     * 
     * If the user does not have permissions, getDataFromSheet throws
     * "GoogleJsonResponseException: 403 Forbidden"
     * This user's credentials are then stored, so this test will fail until
     * they are deleted.
     * 
     * This can't be handled yet cuz exact structure hasnt been determined
     * for the project.
     */
    @Test
    void testGetDataFromSheet() throws IOException {
        CredentialsProvider mockProvider = new CredentialsProvider() {
            @Override
            public InputStream getCredentialsStream() {
                return new ByteArrayInputStream(MOCK_CREDENTIALS.getBytes());
            }
        };
        GoogleSheetsService.setCredentialsProvider(mockProvider);

        // Test the method
        List<List<Object>> result = googleSheetsService.getDataFromSheet(SPREADSHEET_ID, "A1:B2");
        
        // Verify the results
        assertNotNull(result);
        // assertEquals(2, result.size());
        // assertEquals("John Doe", result.get(1).get(0));
        // assertEquals("john@example.com", result.get(1).get(1));
    }

    @Test
    void testPollForUpdatesTrue() throws IOException {
        int lastKnownCount = 0;
        ResponseEntity<FormUpdateResponse> response = googleSheetsService.pollForUpdates(SPREADSHEET_ID, lastKnownCount);
        boolean hasNewResponses = response.getBody().isHasNewResponses();
        assertNotNull(response);
        assertTrue(hasNewResponses);
    }

    @Test
    void testPollForUpdatesFalse() throws IOException {
        int lastKnownCount = 3; // the amount of current responses
        ResponseEntity<FormUpdateResponse> response = googleSheetsService.pollForUpdates(SPREADSHEET_ID, lastKnownCount);
        boolean hasNewResponses = response.getBody().isHasNewResponses();
        assertNotNull(response);
        assertFalse(hasNewResponses);
    }
}