# Custom Shell - Java Terminal Implementation

A custom shell/terminal implementation in Java featuring command parsing, execution, I/O redirection, piping, and tab autocomplete functionality.

## Features

### ✅ Implemented

- **Command Parsing & Tokenization**
  - Quote handling (single and double quotes)
  - Escape sequence support
  - Tokenization with proper delimiter handling

- **Built-in Commands**
  - `echo` - Print text to stdout
  - `cat` - Display file contents
  - `cd` - Change directory
  - `pwd` - Print working directory
  - `ls` - List directory contents
  - `clear` - Clear terminal screen
  - `exit` - Exit shell

- **I/O Redirection**
  - Stdout redirection (`>`, `>>`)
  - Stderr redirection (`2>`, `2>>`)
  - Combined redirection (`&>`, `&>>`)
  - Append and truncate modes

- **Pipe Operator (`|`)**
  - Multi-stage pipeline support
  - Thread-based execution for external commands

- **Tab Autocomplete**
  - File and directory path completion
  - Command name completion

- **External Command Execution**
  - Support for system commands
  - Proper stdin/stdout/stderr handling

## Architecture

### Core Components

- **Parser** (`Parser.java`) - Tokenizes input with quote and escape handling
- **Command Interface** - Abstract interface for all commands
- **PipeHandler** - Manages pipeline execution across builtin and external commands
- **RedirectionContext** - Handles I/O stream redirection
- **ExecutableHandler** - Executes external system commands
- **BuiltInCmdHandler** - Executes internal Java-based commands

### Pipeline Architecture

The pipe implementation uses a three-tier approach:
1. **Pure Builtin Pipelines** - Java-to-Java data flow using memory streams
2. **Pure External Pipelines** - Process-to-Process piping using OS pipes
3. **Mixed Pipelines** - Bridge between Java memory and OS processes

## Technology Stack

- **Language:** Java 17+
- **Build Tool:** Maven
- **Dependencies:**
  - JLine 3.x - Terminal handling and readline functionality
  - Spring Boot (parent POM structure)

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.6+

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

Or run the compiled JAR:

```bash
java -jar target/my-shell-0.0.1-SNAPSHOT.jar
```

## Usage Examples

### Basic Commands
```bash
$ echo "Hello, World!"
Hello, World!

$ pwd
/home/user/projects

```

### I/O Redirection
```bash
$ echo "log entry" > output.txt
$ cat file.txt 2> errors.log
$ pwd &> combined.log
```

### Pipelines
```bash
# Pure builtin pipeline
$ echo "test" | cat

# Pure external pipeline
$ cat file.txt | grep "pattern" | wc -l

# Mixed pipeline
$ echo "data" | external-command | cat
```

### Tab Autocomplete
- Press `Tab` to autocomplete file paths
- Press `Tab` to autocomplete command names
- Double `Tab` to show all available completions

## Project Structure

```
my-shell/
├── src/main/java/com/bhavesh/shell/
│   ├── Main.java                    # Entry point
│   ├── Parser.java                  # Input tokenization
│   ├── Command.java                 # Command interface
│   ├── PipeHandler.java             # Pipeline execution
│   ├── RedirectionContext.java      # I/O redirection
│   ├── ExecutableHandler.java       # External command execution
│   ├── BuiltInCmdHandler.java       # Builtin command execution
│   ├── StreamCopier.java            # Thread-based stream copying
│   └── commands/
│       ├── EchoCommand.java
│       ├── CatCommand.java
│       ├── CdCommand.java
│       ├── PwdCommand.java
│       ├── LsCommand.java
│       ├── ClearCommand.java
│       └── ExitCommand.java
├── pom.xml
└── README.md
```

## Author

Bhavesh - Third-year Computer Science Student

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Inspired by Unix shell design principles
- Built as a learning project to understand shell internals

---

**Note:** This is an educational project built for learning purposes and portfolio demonstration. It is not intended for production use.
