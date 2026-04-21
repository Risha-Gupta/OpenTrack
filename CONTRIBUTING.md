# Contributing to OpenTrack

Thank you for contributing to OpenTrack!

## How to Contribute

### Reporting Bugs
Open an issue with the `bug` label. Include:
- Steps to reproduce the issue
- Expected vs actual behavior
- Your Java version and OS

### Submitting Code

1. Fork the repository
2. Create a branch: `git checkout -b feature/your-feature-name`
3. Write code following the existing patterns
4. Add tests for your changes
5. Ensure tests pass: `./mvnw test`
6. Open a Pull Request with a clear description

## Code Style
- Follow standard Java conventions
- Use Lombok for boilerplate reduction
- Write tests for all service methods
- Keep controllers thin (delegate to services)

## Commit Message Format
`<type>: <short description>`

Types: `feat`, `fix`, `docs`, `test`, `refactor`, `chore`

Example: `feat: add weekly report endpoint`