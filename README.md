<!-- PROJECT SHIELDS -->

[![Contributors](https://img.shields.io/badge/CONTRIBUTORS-01-blue?style=plastic)](https://github.com/ZouariOmar/AgriGO/graphs/contributors)
[![Forks](https://img.shields.io/badge/FORKS-00-blue?style=plastic)](https://github.com/ZouariOmar/AgriGO/network/members)
[![Stargazers](https://img.shields.io/badge/STARS-01-blue?style=plastic)](https://github.com/ZouariOmar/AgriGO/stargazers)
[![Issues](https://img.shields.io/badge/ISSUES-00-blue?style=plastic)](https://github.com/ZouariOmar/AgriGO/issues)
[![GPL3.0 License](https://img.shields.io/badge/LICENSE-GPL3.0-blue?style=plastic)](LICENSE)
[![Linkedin](https://img.shields.io/badge/Linkedin-6.4k-blue?style=plastic)](https://www.linkedin.com/in/zouari-omar)

<h1 align="center">
  <br>
  <a href="doc/logo.png"><img src="doc/logo.png" alt="logo.png" width="300"></a>
  <br>
  hire-log
  <br>
</h1>

<h6 align="center">hire-log is a lightweight desktop app built using JavaFX, SQLite, and FXML to help job seekers log and manage their job applications. With an intuitive UI, users can register, log in, track application events (e.g., applied, interviewed, rejected), and reset passwords.</h6>

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=white)
![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
![Neovim](https://img.shields.io/badge/NeoVim-%2357A143.svg?&style=for-the-badge&logo=neovim&logoColor=white)
![Scene Builder](https://img.shields.io/badge/scene%20builder-%23FF9A00.svg?style=for-the-badge)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)
![Bash Script](https://img.shields.io/badge/bash_script-%23121011.svg?style=for-the-badge&logo=gnu-bash&logoColor=white)

<p align="center">
  <a href="#key-features">Key Features</a> •
  <a href="#how-to-use">How To Use</a> •
  <a href="#project-structure">Project Structure</a> •
  <a href="#built-with">Built With</a> •
  <a href="#contributions">Contributions</a> •
  <a href="#license">License</a> •
  <a href="#contact">Contact</a>
</p>

<p align="center">
  <a href="doc/" target="_blank">
    <img src="doc/snapshots.gif" alt="snapshots.gif">
  </a>
</p>

## Key Features

- User Authentication (Login, Sign up, Forget Password)
- Track job applications with events (applied, interviewed, rejected, etc.)
- Date-stamped entries
- Add comments per application
- Persist data using SQLite
- Clean and modern JavaFX UI
- Local storage, no internet required
- Modify/delete job records easily

## How to Use

### Prerequisites

- Java JDK 17 or later
- JavaFX SDK ([https://openjfx.io/](https://openjfx.io/))
- SQLite JDBC driver
- Maven

> See [pom.xml](https://raw.githubusercontent.com/ZouariOmar/hire-log/refs/heads/main/project/pom.xml) for more details

### Run the app

Clone the repository and run via your IDE or command line:

```bash
# Clone this repo
$ git clone https://github.com/ZouariOmar/hire-log

# Open in your IDE and run the main class (hire-logApp.java)
# ...

# Or if you are using Linux you can run
cd hire-log && ./jrun --install && ./jrun -r
```

## Project Structure

```bash
hire-log/
├── AUTHORS
├── CHANGELOG.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
├── doc
│   ├── logo.png
│   ├── snapshots.gif
│   └── TODO.md
├── jrun
├── LICENSE
├── project
│   ├── database
│   │   └── hirelog.db
│   ├── pom.xml
│   ├── sql
│   │   └── schema.sql
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── mycompany
│   │   │   │           └── hirelog
│   │   │   │               ├── controller
│   │   │   │               │   ├── DashboardController.java
│   │   │   │               │   ├── FrogetPasswordController.java
│   │   │   │               │   ├── hire-logFormController.java
│   │   │   │               │   ├── LoginController.java
│   │   │   │               │   └── SignUpController.java
│   │   │   │               ├── dao
│   │   │   │               │   ├── DatabaseManager.java
│   │   │   │               │   ├── hire-logConnector.java
│   │   │   │               │   └── UserConnector.java
│   │   │   │               ├── flag
│   │   │   │               │   └── hire-logEvents.java
│   │   │   │               ├── hire-logApp.java
│   │   │   │               ├── model
│   │   │   │               │   ├── hire-log.java
│   │   │   │               │   └── User.java
│   │   │   │               ├── service
│   │   │   │               │   ├── MailSenderService.java
│   │   │   │               │   └── PasswordGeneratorService.java
│   │   │   │               └── view
│   │   │   │                   ├── LogTableUi.java
│   │   │   │                   └── ViewUtils.java
│   │   │   └── resources
│   │   │       ├── assets
│   │   │       │   ├── banner.png
│   │   │       │   ├── creative-jobs.png
│   │   │       │   ├── icons8-add-50.png
│   │   │       │   ├── icons8-delete-50.png
│   │   │       │   ├── icons8-delete.gif
│   │   │       │   ├── icons8-edit-50.png
│   │   │       │   ├── icons8-refresh-32.png
│   │   │       │   ├── icons8-refresh.gif
│   │   │       │   ├── icons8-send-mail-50.png
│   │   │       │   ├── icons8-send-mail.gif
│   │   │       │   ├── icons8-upload-24.png
│   │   │       │   ├── logo-1.png
│   │   │       │   ├── logo.png
│   │   │       │   └── Remote-Work-Dice.png
│   │   │       ├── fxml
│   │   │       │   ├── Dashboard.fxml
│   │   │       │   ├── ForgetPassword.fxml
│   │   │       │   ├── hire-logForm.fxml
│   │   │       │   ├── Login.fxml
│   │   │       │   └── SignUp.fxml
│   │   │       ├── log4j2.xml
│   │   │       └── styles
│   │   │           └── Styles.css
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── mycompany
│   │                   └── hire-log
│   │                       └── AppTest.java
├── README.md
└── SECURITY.md
```

## Built With

- [Java](https://www.oracle.com/java/)
- [JavaFX](https://openjfx.io/)
- [SQLite](https://www.sqlite.org/index.html)
- [SceneBuilder](https://gluonhq.com/products/scene-builder/)

## Contributions

Contributions are welcome to expand and improve the repository! Here's how you can contribute:

1. **Fork** this repository.
2. **Clone** your fork:

   ```bash
   git clone https://github.com/ZouariOmar/hire-log.git
   ```

3. **Create a new branch** for your feature:

   ```bash
   git checkout -b feature/my-feature
   ```

4. **Commit your changes**:

   ```bash
   git commit -m ":)"
   ```

5. **Push** your branch:

   ```bash
   git push origin feature/my-feature
   ```

6. Open a **pull request** for review.

## License

This repository is licensed under the **GPL License**. You are free to use, modify, and distribute the content. See the [LICENSE](LICENSE) file for details.

## Contact

For questions or suggestions, feel free to reach out:

- **GitHub**: [ZouariOmar](https://github.com/ZouariOmar)
- **Email**: [zouariomar20@gmail.com](mailto:zouariomar20@gmail.com)
- **LinkedIn**: [Zouari Omar](https://www.linkedin.com/in/zouari-omar)

<p align="center">
  <a href="doc/" target="_blank">
    <img src="doc/meme.png" alt="meme.png">
  </a>
</p>

> Happy Coding!
