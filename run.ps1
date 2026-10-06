# run.ps1 — Compile and run Memory Puzzle
# Usage: Right-click -> Run with PowerShell   OR   pwsh -File run.ps1

$Root = $PSScriptRoot

# Ensure output directory exists
if (-not (Test-Path "$Root\out")) {
    New-Item -ItemType Directory -Path "$Root\out" | Out-Null
}

Write-Host "Compiling Memory Puzzle..." -ForegroundColor Cyan

$sources = Get-ChildItem -Recurse -Filter *.java "$Root\src" | Select-Object -ExpandProperty FullName
$result  = javac -encoding UTF-8 -d "$Root\out" $sources 2>&1

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation FAILED:" -ForegroundColor Red
    Write-Host $result
    exit 1
}

Write-Host "Compilation successful. Starting game..." -ForegroundColor Green
java -cp "$Root\out" Main
