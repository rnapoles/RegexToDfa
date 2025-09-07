#!/usr/bin/env python3
"""
Script to move passing Maven test files from test directory to completed directory.
This script runs Maven tests and moves files that pass to a completed directory.
"""

import os
import shutil
import subprocess
import re
import sys
from pathlib import Path

# Configuration
SOURCE_DIR = r"C:\Work\IA\Parser\AF\RegexToDfa\src\test\java\org\example"
TARGET_DIR = r"C:\Work\IA\Parser\AF\RegexToDfa\src\completed\java\org\example"
PROJECT_ROOT = r"C:\Work\IA\Parser\AF\RegexToDfa"

def is_git_repository():
    """Check if the project is in a git repository."""
    try:
        os.chdir(PROJECT_ROOT)
        result = subprocess.run(
            ["git", "rev-parse", "--git-dir"],
            capture_output=True,
            text=True,
            timeout=10
        )
        if result.returncode == 0:
            print("✓ Git repository detected")
            return True
        else:
            print("No git repository found")
            return False
    except (subprocess.TimeoutExpired, FileNotFoundError):
        print("Git not found or not accessible")
        return False
    except Exception as e:
        print(f"Error checking git status: {e}")
        return False

def git_move_file(source_path, target_path):
    """Move file using git mv command."""
    try:
        # Convert to relative paths for git
        project_root = Path(PROJECT_ROOT)
        source_rel = Path(source_path).relative_to(project_root)
        target_rel = Path(target_path).relative_to(project_root)
        
        # Create target directory first if needed
        target_dir = target_rel.parent
        target_dir_full = project_root / target_dir
        target_dir_full.mkdir(parents=True, exist_ok=True)
        
        # Use git mv
        result = subprocess.run(
            ["git", "mv", str(source_rel), str(target_rel)],
            capture_output=True,
            text=True,
            cwd=PROJECT_ROOT,
            timeout=30
        )
        
        if result.returncode == 0:
            return True, None
        else:
            return False, result.stderr
            
    except Exception as e:
        return False, str(e)

def run_maven_test():
    """Run Maven test and capture output."""
    try:
        print("Running Maven tests...")
        os.chdir(PROJECT_ROOT)
        
        # Run Maven test with detailed output
        result = subprocess.run(
            ["mvn.cmd", "test", "-Dtest=*", "--batch-mode"],
            capture_output=True,
            text=True,
            timeout=300  # 5 minute timeout
        )
        
        print(f"Maven exit code: {result.returncode}")
        return result.stdout + result.stderr, result.returncode
        
    except subprocess.TimeoutExpired:
        print("Error: Maven test execution timed out")
        return None, 1
    except FileNotFoundError:
        print("Error: Maven not found. Please ensure Maven is installed and in PATH")
        return None, 1
    except Exception as e:
        print(f"Error running Maven: {e}")
        return None, 1

def parse_test_results(output):
    """Parse Maven output to identify passing and failing tests."""
    if not output:
        return set(), set()
    
    passing_tests = set()
    failing_tests = set()
    
    # Pattern for test results in Maven Surefire output
    test_patterns = [
        r"Tests run: \d+, Failures: 0, Errors: 0, Skipped: \d+, Time elapsed: .* - in (.+)",
        r"\[INFO\] Running (.+)",
    ]
    
    # Pattern for failed tests
    failure_patterns = [
        r"Tests run: \d+, Failures: [1-9]\d*, Errors: \d+.*- in (.+)",
        r"Tests run: \d+, Failures: \d+, Errors: [1-9]\d*.*- in (.+)",
        r"\[ERROR\].*\.(.+Test)",
    ]
    
    lines = output.split('\n')
    current_test_class = None
    
    for line in lines:
        # Check for running test
        running_match = re.search(r"\[INFO\] Running (.+)", line)
        if running_match:
            current_test_class = running_match.group(1)
            continue
            
        # Check for test completion with results
        for pattern in test_patterns:
            match = re.search(pattern, line)
            if match:
                test_class = match.group(1)
                # Check if this line indicates success (no failures or errors)
                if "Failures: 0" in line and "Errors: 0" in line:
                    passing_tests.add(test_class)
                    print(f"✓ Passing test found: {test_class}")
                break
        
        # Check for failures
        for pattern in failure_patterns:
            match = re.search(pattern, line)
            if match:
                test_class = match.group(1)
                failing_tests.add(test_class)
                print(f"✗ Failing test found: {test_class}")
                break
    
    # Alternative: parse from test summary
    summary_section = False
    for line in lines:
        if "Results:" in line:
            summary_section = True
            continue
        
        if summary_section and "Tests run:" in line:
            if "Failures: 0" in line and "Errors: 0" in line:
                print("All tests in suite passed")
            break
    
    return passing_tests, failing_tests

def find_test_files():
    """Find all Java test files in the source directory."""
    test_files = []
    source_path = Path(SOURCE_DIR)
    
    if not source_path.exists():
        print(f"Source directory does not exist: {SOURCE_DIR}")
        return []
    
    for file_path in source_path.rglob("*.java"):
        if file_path.is_file():
            test_files.append(file_path)
            
    print(f"Found {len(test_files)} Java files in test directory")
    return test_files

def get_class_name_from_file(file_path):
    """Extract the fully qualified class name from a Java file."""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Extract package name
        package_match = re.search(r'package\s+([\w.]+);', content)
        package_name = package_match.group(1) if package_match else ""
        
        # Extract class name
        class_match = re.search(r'public\s+class\s+(\w+)', content)
        if not class_match:
            return None
            
        class_name = class_match.group(1)
        
        # Combine package and class name
        if package_name:
            return f"{package_name}.{class_name}"
        else:
            return class_name
            
    except Exception as e:
        print(f"Error reading file {file_path}: {e}")
        return None

def move_passing_test_files(passing_tests, test_files, use_git=False):
    """Move test files that correspond to passing tests."""
    target_path = Path(TARGET_DIR)
    target_path.mkdir(parents=True, exist_ok=True)
    
    moved_files = []
    
    for file_path in test_files:
        class_name = get_class_name_from_file(file_path)
        if not class_name:
            continue
            
        if class_name in passing_tests:
            try:
                # Calculate relative path from source directory
                source_path = Path(SOURCE_DIR)
                relative_path = file_path.relative_to(source_path)
                
                # Create target file path
                target_file_path = target_path / relative_path
                
                if use_git:
                    # Use git mv
                    success, error = git_move_file(file_path, target_file_path)
                    if success:
                        moved_files.append((file_path, target_file_path))
                        print(f"Git moved: {file_path.name} -> {target_file_path}")
                    else:
                        print(f"Git move failed for {file_path.name}: {error}")
                        # Fallback to regular move
                        print("Falling back to regular file move...")
                        target_file_path.parent.mkdir(parents=True, exist_ok=True)
                        shutil.move(str(file_path), str(target_file_path))
                        moved_files.append((file_path, target_file_path))
                        print(f"Regular moved: {file_path.name} -> {target_file_path}")
                else:
                    # Regular file move
                    target_file_path.parent.mkdir(parents=True, exist_ok=True)
                    shutil.move(str(file_path), str(target_file_path))
                    moved_files.append((file_path, target_file_path))
                    print(f"Moved: {file_path.name} -> {target_file_path}")
                
            except Exception as e:
                print(f"Error moving file {file_path}: {e}")
    
    return moved_files

def main():
    """Main execution function."""
    print("=== Maven Test File Mover ===")
    print(f"Source: {SOURCE_DIR}")
    print(f"Target: {TARGET_DIR}")
    print(f"Project: {PROJECT_ROOT}")
    print()
    
    # Check if directories exist
    if not os.path.exists(PROJECT_ROOT):
        print(f"Error: Project root directory does not exist: {PROJECT_ROOT}")
        sys.exit(1)
    
    if not os.path.exists(SOURCE_DIR):
        print(f"Error: Source directory does not exist: {SOURCE_DIR}")
        sys.exit(1)
    
    # Check if this is a git repository
    use_git = is_git_repository()
    
    # Find test files
    test_files = find_test_files()
    if not test_files:
        print("No test files found to process")
        sys.exit(1)
    
    # Run Maven tests
    output, exit_code = run_maven_test()
    if output is None:
        print("Failed to run Maven tests")
        sys.exit(1)
    
    # Parse test results
    passing_tests, failing_tests = parse_test_results(output)
    
    print(f"\nTest Results Summary:")
    print(f"Passing tests: {len(passing_tests)}")
    print(f"Failing tests: {len(failing_tests)}")
    
    if not passing_tests:
        print("No passing tests found to move")
        
        # Show some Maven output for debugging
        print("\n--- Maven Output (last 20 lines) ---")
        lines = output.split('\n')
        for line in lines[-20:]:
            if line.strip():
                print(line)
        sys.exit(0)
    
    # Move passing test files
    moved_files = move_passing_test_files(passing_tests, test_files, use_git)
    
    print(f"\n=== Summary ===")
    print(f"Files moved: {len(moved_files)}")
    if use_git:
        print("Files moved using git mv (preserves git history)")
    else:
        print("Files moved using regular file operations")
        
    for source, target in moved_files:
        print(f"  {source.name}")
    
    if moved_files:
        print(f"\nAll passing test files have been moved to: {TARGET_DIR}")
        if use_git:
            print("\nNote: The files have been moved using 'git mv' so the move is tracked in git.")
            print("Don't forget to commit these changes with: git commit -m 'Move passing tests to completed directory'")
    else:
        print("No files were moved. This could mean:")
        print("- No tests are currently passing")
        print("- Test class names don't match file names")
        print("- Maven output format is different than expected")

if __name__ == "__main__":
    main()
