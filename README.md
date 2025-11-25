# By Team 'BetterBlueprint': Danni Luo, Akshar Patel, Parthiv Paul, Hetvi Soni, Daniel Yap, Rachel Zhu

A desktop Java application that allows users to log daily health metrics (sleep, water, exercise, calories), calculate personalized health scores, and receive AI-generated insights using the Gemini API.

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven
- A Google Gemini API key

### Setting Up Your Gemini API Key

This application requires a Gemini API key to calculate health scores and generate personalized feedback. Follow these steps to set it up:

#### 1. Get Your Gemini API Key

1. Visit [Google AI Studio](https://aistudio.google.com/app/apikey)
2. Sign in with your Google account
3. Click "Create API Key"
4. Copy the generated API key

#### 2. Set the Environment Variable

The application looks for an environment variable named `GEMINI_API_KEY`. Choose one of the methods below:

##### **Option A: Set in IntelliJ IDEA (Recommended for Development)**

1. Open **Run** → **Edit Configurations**
2. Select your main application configuration (or create one if it doesn't exist)
3. Find the **Environment variables** field
4. Click the folder icon to open the environment variables dialog
5. Click the **+** button to add a new variable
6. Enter:
   - **Name:** `GEMINI_API_KEY`
   - **Value:** `your-api-key-here` (paste your actual API key)
7. Click **OK** and **Apply**
8. Run your application

##### **Option B: Set System-Wide (Windows)**

**Using PowerShell (Temporary - current session only):**
```powershell
$env:GEMINI_API_KEY = "your-api-key-here"
```

**Using PowerShell (Permanent - user-level):**
```powershell
[System.Environment]::SetEnvironmentVariable('GEMINI_API_KEY', 'your-api-key-here', 'User')
```

**Using Command Prompt (Permanent):**
```cmd
setx GEMINI_API_KEY "your-api-key-here"
```

**Note:** After setting a permanent environment variable, you must restart IntelliJ IDEA for it to pick up the new value.

##### **Option C: Set System-Wide (macOS/Linux)**

**Temporary (current terminal session):**
```bash
export GEMINI_API_KEY="your-api-key-here"
```

**Permanent (add to your shell profile):**

For Bash (~/.bashrc or ~/.bash_profile):
```bash
echo 'export GEMINI_API_KEY="your-api-key-here"' >> ~/.bashrc
source ~/.bashrc
```

For Zsh (~/.zshrc):
```bash
echo 'export GEMINI_API_KEY="your-api-key-here"' >> ~/.zshrc
source ~/.zshrc
```

#### 3. Verify the Setup

To verify your environment variable is set correctly:

**Windows (PowerShell):**
```powershell
echo $env:GEMINI_API_KEY
```

**Windows (Command Prompt):**
```cmd
echo %GEMINI_API_KEY%
```

**macOS/Linux:**
```bash
echo $GEMINI_API_KEY
```

You should see your API key printed. If not, check that you've set it correctly and restarted your IDE/terminal.

### Running the Application

1. Clone the repository
2. Set up your Gemini API key (see above)
3. Open the project in IntelliJ IDEA
4. Run the `Main.java` file

If the API key is not set, you'll see an error message:
```
IllegalStateException: GEMINI_API_KEY environment variable is not set. 
Please set it before running the application.
```

## Features

- User signup and login
- Daily health metrics input (sleep, water, exercise, calories)
- AI-powered health score calculation using Google Gemini
- Personalized health insights and feedback
- Historical health data tracking

## Security Note

⚠️ **Never commit your API key to version control!** The application uses environment variables to keep your API key secure. Always use environment variables for sensitive credentials.


