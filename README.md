# Telecom Domain: Contact List API Test Automation Framework

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![RestAssured](https://img.shields.io/badge/RestAssured-5.5.0-green)](https://rest-assured.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.12.0-FF7F00?logo=testng&logoColor=white)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![ExtentReports](https://img.shields.io/badge/ExtentReports-5.1.2-blue)](https://www.extentreports.com/)
[![Postman](https://img.shields.io/badge/Postman-v2.1-FF6C37?logo=postman&logoColor=white)](https://www.postman.com/)

---

## 📌 Project Overview
This project is an end-to-end **REST API Test Automation Framework** developed for the **Telecom Domain Contact List Application** ([https://thinking-tester-contact-list.herokuapp.com/](https://thinking-tester-contact-list.herokuapp.com/)).

It satisfies both key deliverables specified in the Capstone requirements:
1. **RestAssured Automation Suite (Java):** A modular API framework built using **RestAssured**, **TestNG**, **Jackson Databind POJOs**, and **ExtentReports 5.x**.
2. **Postman & Chai Test Suite:** A complete Postman collection with dynamic environment chaining and **Chai library assertions** (`pm.test()`, `pm.expect()`).

---

## 🏗️ Framework Architecture

```text
telecom-contactlist-api/
├── pom.xml                                  <-- Maven build file with RestAssured, TestNG, ExtentReports, Jackson
├── testng.xml                               <-- Master TestNG Suite XML
├── README.md                                <-- Complete setup, execution guide, and API specification
├── .gitignore                               <-- Configured for Java, Maven, Eclipse, and Reports
├── .project & .classpath                    <-- Native Eclipse metadata for 1-click import
├── .settings/                               <-- JDT compiler (Java 17) & m2e preferences
├── TestReport/                              <-- Generated ExtentReports HTML dashboards
├── postman/
│   ├── Telecom_Contact_List_API.postman_collection.json <-- All 10 requests with Chai assertions
│   └── Telecom_API_Environment.postman_environment.json <-- Dynamic environment variables
└── src/
    ├── main/
    │   └── java/com/telecom/api/
    │       ├── endpoints/
    │       │   ├── Routes.java              <-- Centralized API endpoint constants
    │       │   ├── UserEndpoints.java       <-- RestAssured CRUD operations for /users
    │       │   └── ContactEndpoints.java    <-- RestAssured CRUD operations for /contacts
    │       ├── payload/                     <-- POJO Serialization/Deserialization models
    │       │   ├── User.java                <-- User model
    │       │   └── Contact.java             <-- Contact model
    │       └── utilities/
    │           ├── ConfigReader.java        <-- Reads baseUrl and config properties
    │           └── ExtentManager.java       <-- Configures ExtentReports HTML reporting
    └── test/
        ├── java/com/telecom/api/
        │   ├── listeners/
        │   │   └── TestListener.java        <-- TestNG listener logging results to ExtentReports
        │   └── tests/
        │       ├── EndToEndWorkflowTest.java<-- Complete 10-step sequence from Add User to Logout
        │       ├── UserTests.java           <-- Focused user module & negative tests
        │       └── ContactTests.java        <-- Focused contact management tests
        └── resources/
            └── config.properties            <-- Base URL and configuration settings
```

---

## 🔄 End-to-End Testing Workflow & API Matrix

The testing sequence covers the full business lifecycle:
$$\text{Add User} \rightarrow \text{Get Profile} \rightarrow \text{Update User} \rightarrow \text{Login User} \rightarrow \text{Add Contact} \rightarrow \text{Get Contact List} \rightarrow \text{Get Contact} \rightarrow \text{Update Contact (PUT)} \rightarrow \text{Update Contact (PATCH)} \rightarrow \text{Logout User}$$

| Step | Test Case | Method | Endpoint | Expected Status | Key Validations |
| :---: | :--- | :---: | :--- | :---: | :--- |
| **TC1** | **Add New User** | `POST` | `/users` | `201 Created` | Token generated, user created. |
| **TC2** | **Get User Profile** | `GET` | `/users/me` | `200 OK` | Bearer token authentication, user details match. |
| **TC3** | **Update User** | `PATCH` | `/users/me` | `200 OK` | First name, last name, email updated. |
| **TC4** | **Log In User** | `POST` | `/users/login` | `200 OK` | Login with updated credentials, refresh Bearer token. |
| **TC5** | **Add Contact** | `POST` | `/contacts` | `201 Created` | Contact saved, capture contact `_id`. |
| **TC6** | **Get Contact List** | `GET` | `/contacts` | `200 OK` | Verify array of contacts contains created contact. |
| **TC7** | **Get Contact by ID** | `GET` | `/contacts/{contactId}` | `200 OK` | Verify contact fields match `contactId`. |
| **TC8** | **Update Contact (Full)** | `PUT` | `/contacts/{contactId}` | `200 OK` | All contact fields replaced (Amy Miller, Canada). |
| **TC9** | **Update Contact (Partial)**| `PATCH` | `/contacts/{contactId}` | `200 OK` | Only `firstName` updated to "Anna". |
| **TC10**| **Logout User** | `POST` | `/users/logout` | `200 OK` | Token invalidated (subsequent calls return 401). |

---

## 🚀 How to Run and Check in Eclipse IDE

### 1. Import Project into Eclipse
1. Open **Eclipse IDE**.
2. Click **File** $\rightarrow$ **Import...**
3. Select **Maven** $\rightarrow$ **Existing Maven Projects**, then click **Next**.
4. Set **Root Directory** to:
   ```text
   C:\Users\<your-username>\eclipse-workspace\telecom-contactlist-api
   ```
5. Check `pom.xml` and click **Finish**. Eclipse will configure the classpath and resolve dependencies automatically.

### 2. Run TestNG Suite in Eclipse
1. In the Eclipse **Package Explorer**, right-click **[`testng.xml`](file:///C:/Users/nagar/eclipse-workspace/telecom-contactlist-api/testng.xml)**.
2. Select **Run As** $\rightarrow$ **TestNG Suite**.
3. All 10 workflow tests and module tests will execute sequentially against the live API.

### 3. Run via Maven Command Line
Open PowerShell or Command Prompt inside the project directory:
```bash
mvn clean test
```

### 4. View Test Reports
* **ExtentReports (HTML Dashboard):** Refresh the project in Eclipse, navigate to `TestReport/`, and open `Telecom_API_Report_<timestamp>.html` in your web browser.
* **TestNG Reports:** Generated under `test-output/index.html` and `test-output/emailable-report.html`.

---

## 📮 How to Run and Check in Postman

### 1. Import Collection & Environment
1. Open **Postman**.
2. Click the **Import** button (top left).
3. Drag and drop both files from the `postman/` folder:
   * `Telecom_Contact_List_API.postman_collection.json`
   * `Telecom_API_Environment.postman_environment.json`
4. In Postman, select **Telecom API Environment** from the environment dropdown in the top-right corner.

### 2. Execute via Postman Collection Runner
1. Click on the collection **Telecom Domain - Contact List API Testing**.
2. Click **Run collection**.
3. Ensure all 10 requests are selected in order.
4. Click **Run Telecom Domain - Contact List API Testing**.
5. All 10 requests will execute with 100% passing Chai assertions (`pm.test`).

### 3. Run via Newman (CLI)
If you have Newman installed:
```bash
newman run postman/Telecom_Contact_List_API.postman_collection.json -e postman/Telecom_API_Environment.postman_environment.json
```
