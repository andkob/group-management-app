# Planly

This web application serves as a centralized hub for coordinating and managing schedules within groups such as fraternities and other organizations. Initially designed to integrate with Google Sheets to pull schedule data entered by members via a Google Form, the app will evolve to facilitate efficient event planning by visualizing overlapping availabilities. Future features will include creating and managing events, tracking upcoming activities, and enhancing communication among members. By building on the foundation of individual schedules, this application aims to streamline coordination and foster collaboration within the group.

## Features
- OAuth 2.0 Authentication with Google for secure access to Google Sheets.
- Reads schedule data from Google Sheets.
- Backend implemented using **Spring Boot** as the framework.
- Frontend built with **React**.
- Maven-based project for build and dependency management.

## 🚧 Getting Started
***Note:** This project is currently in development. Please follow the instructions below only if you are interested in building on it or trying out the existing features.*

### Prerequisites

Before running the application, ensure that you have the following installed:
- **Java JDK 21** or later
- **Maven** for project building and dependency management
- A **Google Cloud Project** set up with OAuth 2.0 credentials
- The latest version of **npm** (node package manager)

### Setting up Google Cloud OAuth

1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a project if you haven't already.
3. Enable the **Google Sheets API** and **Google Forms API** for the project.
4. Create OAuth 2.0 credentials and download the `credentials.json` file.
5. Place the `credentials.json` file in the `src/main/resources/` directory of this project. **TEMPORARY** (dont worry)
6. Add `http://localhost:8888/Callback` as an authorized redirect URI in the Google Cloud Console.

### Project Structure
(*omitted for now*)

### Running the Application
The following steps will walk through how to set up the project and run it. You will connect to the React dev server, which is currently the front-end access point.
1. Clone the repository
```
$ git clone https://github.com/andkob/group-scheduler.git
$ cd group-scheduler
```
2. Build the project with maven
(*must be run with the -DskipTests flag cuz all the tests will fail and the build process will explode*)
```
$ mvn clean install -DskipTests
```
3. From the project root directory, run the backend server (*requests will be handled on port 8080*)
```
$ mvn spring-boot:run
```
4. Open a new terminal and install the frontend dependencies
```
$ cd src/main/webapp/frontend
$ npm install
```
5. From the same directory, start the React development server
```
$ npm start
```
6. Access the app at `http://localhost:3000`

### Using Google OAuth 2.0

Upon first run, the app will prompt you to authenticate with Google. You will be directed to a Google sign-in page and, after successful authentication, be redirected back to the application.

Ensure that you have read access to the Google Sheet that contains the schedule data.

### GoogleSheetsService

This service handles interactions with Google Sheets via the Google Sheets API. It uses OAuth 2.0 for authentication and authorization.

- **Method: `getDataFromSheet(String spreadsheetID, String range)`**
  Retrieves data from the specified Google Sheet.
