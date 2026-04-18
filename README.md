Naukri Profile Auto Update 🚀

📌 Overview
This project is an automation framework built using Playwright + Java + TestNG + Cucumber to automatically update a Naukri profile on a daily basis.
The workflow is scheduled using GitHub Actions to run every day at 9:00 AM IST.
________________________________________
⚙️ Tech Stack

•	Playwright (UI Automation)
•	Java
•	TestNG
•	Cucumber (BDD)
•	GitHub Actions (CI/CD)
________________________________________
🔄 How It Works

1.	The GitHub Actions workflow is triggered daily using a cron schedule.
2.	The automation script launches the browser in headless mode.
3.	Instead of logging in every time (which triggers OTP/authentication issues), the script uses a saved session:
o	auth.json stores session cookies and login state.
4.	The script directly navigates to the profile section.
5.	Updates the required profile details (e.g., resume headline).
6.	Verifies the update using assertions.
________________________________________
🔐 Handling Authentication (Important)

Since the application has OTP-based login and bot detection, direct login from CI fails.
Solution Implemented:
•	A session file (auth.json) is generated after successful login locally.
•	This file contains:
o	Cookies
o	Local storage
o	Session data
Security Approach:
•	auth.json is NOT stored in the repository
•	It is:
1.	Converted to Base64 string
2.	Stored securely in GitHub Secrets
3.	Recreated during workflow execution
________________________________________
🔑 Secrets Used

The following secrets are configured in GitHub:
•	NAUKRI_USERNAME → Naukri login username
•	NAUKRI_PASSWORD → Naukri login password
•	AUTH_JSON → Base64 encoded session file
________________________________________
🛠️ Workflow Behavior

In Local Execution:
•	Performs full login
•	Generates auth.json
•	Saves session for reuse
In GitHub Actions (CI):
•	Skips login step
•	Recreates auth.json from secrets
•	Uses stored session to continue execution
________________________________________
📂 Project Highlights

•	Handles real-world authentication challenges (OTP, bot detection)
•	CI/CD enabled with GitHub Actions
•	Secure handling of sensitive data using secrets
•	Headless execution optimized for CI environments
•	Clean BDD structure using Cucumber
________________________________________
⚠️ Note

•	GitHub scheduled workflows may not run exactly at the scheduled time due to queue delays.
•	If the session expires, auth.json needs to be regenerated and updated in secrets.
________________________________________
👩‍💻 Author

Automation framework built as a real-world simulation of handling secure login systems and CI execution challenges.

