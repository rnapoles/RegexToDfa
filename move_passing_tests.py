#!/usr/bin/env python3
"""
Script to move passing Maven test files from test directory to completed directory.
This script runs Maven tests individually and moves files that pass to a completed directory.
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

def find_all_test_classes():
    """Find all test classes in the source directory."""
    test_classes = []
    source_path = Path(SOURCE_DIR)
    
    if not source_path.exists():
        print(f"Source directory does not exist: {SOURCE_DIR}")
        return []
    
    for file_path in source_path.rglob("*.java"):
        if file_path.is_file():
            class_name = get_class_name_from_file(file_path)
            if class_name and (class_name.endswith("Test") or "test" in class_name.lower()):
                test_classes.append((class_name, file_path))
    
    print(f"Found {len(test_classes)} test classes")
    return test_classes

def run_single_maven_test(test_class):
    """Run a single Maven test and capture output."""
    try:
        print(f"Running test: {test_class}")
        os.chdir(PROJECT_ROOT)
        
        # Run single Maven test with detailed output
        result = subprocess.run(
            ["mvn.cmd", "test", f"-Dtest={test_class}", "--batch-mode"],
            capture_output=True,
            text=True,
            timeout=120  # 2 minute timeout per test
        )
        
        return result.stdout + result.stderr, result.returncode
        
    except subprocess.TimeoutExpired:
        print(f"Error: Test {test_class} execution timed out")
        return None, 1
    except FileNotFoundError:
        print("Error: Maven not found. Please ensure Maven is installed and in PATH")
        return None, 1
    except Exception as e:
        print(f"Error running Maven test {test_class}: {e}")
        return None, 1

def run_maven_tests_individually(test_classes):
    """Run each Maven test individually and track results."""
    passing_tests = set()
    failing_tests = set()
    
    print(f"\n=== Running {len(test_classes)} tests individually ===")
    
    for i, (test_class, file_path) in enumerate(test_classes, 1):
        print(f"\n[{i}/{len(test_classes)}] Testing: {test_class}")
        
        output, exit_code = run_single_maven_test(test_class)
        
        if output is None:
            print(f"✗ {test_class} - Failed to run")
            failing_tests.add(test_class)
            continue
        
        # Check if test passed
        if exit_code == 0 and ("BUILD SUCCESS" in output or "Tests run:" in output):
            # More detailed check for actual test results
            if "Failures: 0" in output and "Errors: 0" in output:
                print(f"✓ {test_class} - PASSED")
                passing_tests.add(test_class)
            else:
                print(f"✗ {test_class} - FAILED (has failures/errors)")
                failing_tests.add(test_class)
        else:
            print(f"✗ {test_class} - FAILED (build failed)")
            failing_tests.add(test_class)
    
    return passing_tests, failing_tests

def move_passing_test_files(passing_tests, test_classes_with_files, use_git=False):
    """Move test files that correspond to passing tests."""
    target_path = Path(TARGET_DIR)
    target_path.mkdir(parents=True, exist_ok=True)
    
    moved_files = []
    
    # Create a mapping from class name to file path
    class_to_file = {class_name: file_path for class_name, file_path in test_classes_with_files}
    
    for test_class in passing_tests:
        if test_class in class_to_file:
            file_path = class_to_file[test_class]
            
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
    
    # Find all test classes
    test_classes_with_files = find_all_test_classes()
    if not test_classes_with_files:
        print("No test classes found to process")
        sys.exit(1)
    
    print(f"\nFound test classes:")
    for class_name, file_path in test_classes_with_files:
        print(f"  - {class_name} ({file_path.name})")
    
    # Ask user for confirmation
    # response = input(f"\nDo you want to run all {len(test_classes_with_files)} tests individually? (y/N): ")
    # if response.lower() not in ['y', 'yes']:
        # print("Cancelled by user")
        # sys.exit(0)
    
    # Run Maven tests individually
    passing_tests, failing_tests = run_maven_tests_individually(test_classes_with_files)
    
    print(f"\n=== Test Results Summary ===")
    print(f"Passing tests: {len(passing_tests)}")
    print(f"Failing tests: {len(failing_tests)}")
    
    if passing_tests:
        print(f"\n✓ Passing tests:")
        for test in sorted(passing_tests):
            print(f"  - {test}")
    
    if failing_tests:
        print(f"\n✗ Failing tests:")
        for test in sorted(failing_tests):
            print(f"  - {test}")
    
    if not passing_tests:
        print("\nNo passing tests found to move")
        sys.exit(0)
    
    # Ask for confirmation before moving files
    response = input(f"\nDo you want to move {len(passing_tests)} passing test files? (y/N): ")
    if response.lower() not in ['y', 'yes']:
        print("File moving cancelled by user")
        sys.exit(0)
    
    # Move passing test files
    moved_files = move_passing_test_files(passing_tests, test_classes_with_files, use_git)
    
    print(f"\n=== Summary ===")
    print(f"Files moved: {len(moved_files)}")
    if use_git:
        print("Files moved using git mv (preserves git history)")
    else:
        print("Files moved using regular file operations")
        
    for source, target in moved_files:
        print(f"  ✓ {source.name}")
    
    if moved_files:
        print(f"\nAll passing test files have been moved to: {TARGET_DIR}")
        if use_git:
            print("\nNote: The files have been moved using 'git mv' so the move is tracked in git.")
            print("Don't forget to commit these changes with: git commit -m 'Move passing tests to completed directory'")
    else:
        print("No files were moved.")
        
    print(f"\nFinal stats:")
    print(f"  Total tests found: {len(test_classes_with_files)}")
    print(f"  Tests passed: {len(passing_tests)}")
    print(f"  Tests failed: {len(failing_tests)}")
    print(f"  Files moved: {len(moved_files)}")

if __name__ == "__main__":
    main()
