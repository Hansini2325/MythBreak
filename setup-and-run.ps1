param(
    [switch]$SkipDownload,
    [string]$DbPassword = ""
)

$ErrorActionPreference = "Stop"
$MAVEN_VERSION = "3.9.9"
$MAVEN_DIR = "$PSScriptRoot\tools\apache-maven-$MAVEN_VERSION"
$MAVEN_ZIP = "$PSScriptRoot\tools\maven.zip"
$MAVEN_URL = "https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip"

function Write-Header($msg) {
    Write-Host ""
    Write-Host "============================================================" -ForegroundColor Cyan
    Write-Host "  $msg" -ForegroundColor Cyan
    Write-Host "============================================================" -ForegroundColor Cyan
}

function Write-Step($msg) {
    Write-Host "  >> $msg" -ForegroundColor Yellow
}

function Write-OK($msg) {
    Write-Host "  [OK] $msg" -ForegroundColor Green
}

function Write-Fail($msg) {
    Write-Host "  [FAIL] $msg" -ForegroundColor Red
}

# -----------------------------------------------
# STEP 1: Check Java
# -----------------------------------------------
Write-Header "MythBreak Setup Script"
Write-Step "Checking Java..."
try {
    $javaVer = java -version 2>&1 | Select-String "version"
    Write-OK "Java found: $javaVer"
} catch {
    Write-Fail "Java not found! Please install JDK 21+ from https://adoptium.net/"
    exit 1
}

# -----------------------------------------------
# STEP 2: Download Maven (if not present)
# -----------------------------------------------
Write-Step "Checking Maven..."
$mvnExe = "$MAVEN_DIR\bin\mvn.cmd"
if (!(Test-Path $mvnExe)) {
    if ($SkipDownload) {
        Write-Fail "Maven not found at $MAVEN_DIR. Run without -SkipDownload to auto-download."
        exit 1
    }
    Write-Step "Downloading Apache Maven $MAVEN_VERSION..."
    New-Item -ItemType Directory -Force -Path "$PSScriptRoot\tools" | Out-Null
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        Invoke-WebRequest -Uri $MAVEN_URL -OutFile $MAVEN_ZIP -UseBasicParsing
        Write-OK "Download complete."
        Write-Step "Extracting Maven..."
        Expand-Archive -Path $MAVEN_ZIP -DestinationPath "$PSScriptRoot\tools" -Force
        Remove-Item $MAVEN_ZIP -Force
        Write-OK "Maven extracted to $MAVEN_DIR"
    } catch {
        Write-Fail "Failed to download Maven: $_"
        Write-Host ""
        Write-Host "MANUAL STEPS:" -ForegroundColor White
        Write-Host "  1. Download Maven from: https://maven.apache.org/download.cgi" -ForegroundColor White
        Write-Host "  2. Extract it to: $PSScriptRoot\tools\" -ForegroundColor White
        Write-Host "  3. Re-run this script." -ForegroundColor White
        exit 1
    }
} else {
    Write-OK "Maven found at $MAVEN_DIR"
}

$env:PATH = "$MAVEN_DIR\bin;$env:PATH"

# -----------------------------------------------
# STEP 3: MySQL Database Setup
# -----------------------------------------------
Write-Header "Database Setup"
$mysqlExe = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
if (!(Test-Path $mysqlExe)) {
    $mysqlExe = "mysql"
}

if ($DbPassword -eq "") {
    Write-Host ""
    Write-Host "Enter your MySQL ROOT password (or press Enter if no password):" -ForegroundColor White
    $secPwd = Read-Host -AsSecureString
    $cred = New-Object System.Management.Automation.PSCredential("root", $secPwd)
    $DbPassword = $cred.GetNetworkCredential().Password
}

Write-Step "Creating database and user..."
$sqlScript = @"
CREATE DATABASE IF NOT EXISTS mythbreak_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'mythbreak_user'@'localhost' IDENTIFIED BY 'Change_Me_123';
GRANT ALL PRIVILEGES ON mythbreak_db.* TO 'mythbreak_user'@'localhost';
FLUSH PRIVILEGES;
SELECT 'Database setup complete!' AS Status;
"@

$tmpSql = [System.IO.Path]::GetTempFileName() + ".sql"
$sqlScript | Out-File -FilePath $tmpSql -Encoding UTF8

try {
    if ($DbPassword -eq "") {
        & "$mysqlExe" -u root --execute="source $tmpSql" 2>&1 | Out-Null
    } else {
        & "$mysqlExe" -u root -p"$DbPassword" --execute="source $tmpSql" 2>&1 | Out-Null
    }
    Write-OK "Database 'mythbreak_db' and user 'mythbreak_user' created."
    
    # Run schema
    Write-Step "Running schema.sql..."
    if ($DbPassword -eq "") {
        & "$mysqlExe" -u root mythbreak_db --execute="source $PSScriptRoot\database\schema.sql" 2>&1 | Out-Null
    } else {
        & "$mysqlExe" -u root -p"$DbPassword" mythbreak_db --execute="source $PSScriptRoot\database\schema.sql" 2>&1 | Out-Null
    }
    Write-OK "Schema applied."
    
    # Run seed data
    Write-Step "Loading seed data..."
    if ($DbPassword -eq "") {
        & "$mysqlExe" -u root mythbreak_db --execute="source $PSScriptRoot\database\data.sql" 2>&1 | Out-Null
    } else {
        & "$mysqlExe" -u root -p"$DbPassword" mythbreak_db --execute="source $PSScriptRoot\database\data.sql" 2>&1 | Out-Null
    }
    Write-OK "Seed data loaded."
} catch {
    Write-Fail "Database setup failed: $_"
    Write-Host "Try running the SQL manually from MySQL Workbench." -ForegroundColor Yellow
} finally {
    Remove-Item $tmpSql -Force -ErrorAction SilentlyContinue
}

# -----------------------------------------------
# STEP 4: Build Project
# -----------------------------------------------
Write-Header "Building MythBreak"
Write-Step "Running Maven build (this may take 2-5 minutes on first run)..."
Set-Location $PSScriptRoot
& "$MAVEN_DIR\bin\mvn.cmd" clean package -DskipTests -q
if ($LASTEXITCODE -ne 0) {
    Write-Fail "Build failed! Check the output above for errors."
    exit 1
}
Write-OK "Build successful!"

# -----------------------------------------------
# STEP 5: Run
# -----------------------------------------------
Write-Header "Starting MythBreak"
Write-Host ""
Write-Host "  Application starting at: http://localhost:8080" -ForegroundColor Green
Write-Host "  Press Ctrl+C to stop." -ForegroundColor Yellow
Write-Host ""
& java -jar "$PSScriptRoot\target\mythbreak-1.0.0.jar"
