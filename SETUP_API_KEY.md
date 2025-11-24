# Setting up the Gemini API Key

The application now uses an environment variable for the Gemini API key to keep it secure.

## Setting the Environment Variable

### On Windows (PowerShell)

**Temporary (current session only):**
```powershell
$env:GEMINI_API_KEY = "your-api-key-here"
```

**Permanent (user-level):**
```powershell
[System.Environment]::SetEnvironmentVariable('GEMINI_API_KEY', 'your-api-key-here', 'User')
```

After setting it permanently, restart IntelliJ IDEA for it to pick up the new environment variable.

### On Windows (Command Prompt)

**Temporary:**
```cmd
set GEMINI_API_KEY=your-api-key-here
```

**Permanent:**
```cmd
setx GEMINI_API_KEY "your-api-key-here"
```

### On macOS/Linux

**Temporary:**
```bash
export GEMINI_API_KEY="your-api-key-here"
```

**Permanent (add to ~/.bashrc or ~/.zshrc):**
```bash
echo 'export GEMINI_API_KEY="your-api-key-here"' >> ~/.bashrc
source ~/.bashrc
```

## Setting in IntelliJ IDEA

If you prefer to set the environment variable only for running the application in IntelliJ:

1. Go to **Run** → **Edit Configurations**
2. Select your application configuration (or create one)
3. In the **Environment variables** field, add:
   ```
   GEMINI_API_KEY=your-api-key-here
   ```
4. Click **OK**

## Your API Key

Your Gemini API key is:
```
AIzaSyBuI8LXd226lmxwLTvqP6o3i3Mtq6kbsFg
```

**Important:** Keep this key secure and do not commit it to version control!

## Verifying the Setup

To verify the environment variable is set correctly:

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

If the variable is not set when you run the application, you will see an error message:
```
GEMINI_API_KEY environment variable is not set. Please set it before running the application.
```

