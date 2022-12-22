[![Stargazers][stars-shield]][stars-url][![Issues][issues-shield]][issues-url][![MIT License][license-shield]][license-url][![Latest release][latest-release-shield]][latest-release-url]


<p align="center">
    	<img src="https://raw.githubusercontent.com/SeverinBuchser/GitAnalyzer/master/git-analyzer-logo.svg" alt="git-analyzer-logo" width="20%"/> 
</p>
<h1 align="center">
	Git Analyzer
</h1>
<p><sub align"center" size="0.5">
	<img src="https://mirrors.creativecommons.org/presskit/icons/cc.xlarge.png" alt="cc-logo" width="20" background="white"/> 
	Git Analyzer Logo by <a href="https://github.com/SeverinBuchser/">Severin Buchser</a> is licensed under the <a href="https://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported License</a> / <a href="https://git-scm.com/downloads/logos">The original Git-Logo</a> is remixed into the git-analyzer-logo.
</sub></p>


This project is part of a bachelor-thesis in the [Software Engineering Group](https://seg.inf.unibe.ch/) at the [University of Bern](https://www.unibe.ch/). The project is used for the data-collection and data-analyzing process of the thesis. The thesis focuses on the human intervention in merge conflicts: How often are the used merge resolutions not reproducible by a computer?

This tool collects information about merges of git-projects. The information one can collect is how many conflicting merges, conflicting files and conflicting chunks are in a project as well as how many of the previously stated abstractions can be reproduced by a computer.

## Getting Started

To get a local copy up and running follow these simple example steps.

### Prerequisites

You will need to install [Apache Maven](https://maven.apache.org/) to build the project.

* [Apache Maven Download](https://maven.apache.org/download.cgi)
* [Apache Maven Installation Guide](https://maven.apache.org/install.html)

### Installation

1. Clone the repo
   ```shell
   git clone https://github.com/SeverinBuchser/GitAnalyzer.git
   ```

2. Install Dependencies
   ```shell
   mvn clean validate
   ```

3. <a name="make-executable"></a>The intended way of creating an executable is by running the following command, which creates an executable script called "git-analyzer":

   ```shell
   ./make_executable.sh
   ```

   There is also the option to obtain an executable jar (three ways):

   1. Package to the "target" directory:

       ```shell
       mvn clean package
       ```

   2. Install to local maven repository:

       ```shell
       mvn clean install
       ```
   
   3. Download the jar file [here](https://github.com/SeverinBuchser/GitAnalyzer/releases/latest).

# Usage

## Execution

The intended way to execute the tool is to run the "git-analyzer" executable, see [above](#make-executable). If you would choose to create an executable jar yourself, or download the jar, usually with name "GitAnalyzer-1.0.0-jar-with-dependencies.jar", the jar file can be executed:

```shell
java -jar path/to/jar/directory/GitAnalyzer-1.0.0-jar-with-dependencies.jar [..options]
```

Here, the main command `git-analyzer` must not be executed again but rather the [options](#basic-usage) must be supplied. The last way to execute the tool (only predefined executions, intended for development purposes) is described [here](#predefined-executions).

### Basic Usage

```shell
Usage: git-analyzer [-hV] [-c=<configPath>] [COMMAND]
Study merge conflict resolution behaviour of Git projects.
  -c, --config=<configPath>
                  Use either this option or use a sub-command. Path to the
                    config file.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  clone-project
  clone-projects
  analyze-conflicts
```

There is an option to use a JSON config file for the main application. The schema is located at [config.schema.json](https://github.com/SeverinBuchser/GitAnalyzer/blob/master/src/main/resources/config.schema.json) and there are some example configurations [here](https://github.com/SeverinBuchser/GitAnalyzer/tree/master/configs). The config file can be specified by using the option `-c=<configPath>` without any subcommands.

#### Cloning Projects

<a name="clone-project"></a>

```shell
Usage: git-analyzer clone-project [-pd=<projectDir>] <name> <url>
      <name>   The name of the project. The project will be cloned into a
                 directory with this name.
      <url>    The url from which the project can be cloned from.
      -pd, --project-dir=<projectDir>
               The directory where the project will be cloned to. Defaults to
                 working directory.
```

To clone one project, use the `clone-project` subcommand, which takes the name and URL of the project. The name can be anything, just remember the name for later usage. The intended way of the name would be `user_name/repo_name`. The URL is for example: https://github.com/SeverinBuchser/GitAnalyzer.

<a name="clone-projects"></a>

```shell
Usage: git-analyzer clone-projects [-pd=<projectDir>] <projectListPath>
      <projectListPath>
      -pd, --project-dir=<projectDir>
                          The directory where the projects will be cloned to.
                            Defaults to working directory.
```

To clone multiple projects, use the `clone-projects` subcommand, which takes a path to a CSV file, in which the projects to be cloned are located. This file is called a project list and it is used later as well, **so if you plan to use this tool, make sure to create such a project list**:

```
# example project list CSV file
name,url
name_one,url_one
name_two,url_two
...
```

The option `project-dir`, used in both clone-subcommands, defaults to the current working directory. It denotes the location, to which the projects will be cloned to.

#### Analyze Conflicts

```shell
Usage: git-analyzer analyze-conflicts [-od=<outDir>] [-os=<outSuffix>]
                             [-pd=<projectDir>] <projectListPath>
      <projectListPath>   The path to the project list CSV file.
      -od, --out-dir=<outDir>
                          The output directory, where the output JSON file
                            goes. Defaults to working directory.
      -os, --out-suffix=<outSuffix>
                          The suffix for the output JSON file. Default is no
                            suffix.
      -pd, --project-dir=<projectDir>
                          The directory where the cloned projects are located.
                            Defaults to working directory.
```

This command analyzes projects of a project list. The parameter `projectListPath`, as well as the option `project-dir` are the same as in the [clone-projects subcommand](#clone-projects). The option `out-dir` defaults to the current working directory. It is the directory to where the output file will be written to. The output file is named as follows: Assume you have the following execution:

- `projectListPath`: "path/to/project_list/project_list_name.csv"
- `out-dir`: "path/to/output_directory"
- `out-suffix`: "some-suffix"
- `project-dir`: "path/to/project_directory"

With these parameters and options, the output file will be created as: "path/to/output_directory/project_list_name-some-suffix.json".

The `outDir` is the directory for the output files, default is `./`. The `outSuffix` is the part which is appended to the output files name. The output file will be a CSV file. The rest is the same as in the other commands. The only difference is the `projectDir` which is now the directory where the cloned projects are located.

### Predefined Executions

Predefined executables are already provided in the maven configuration, [pom.xml](https://github.com/SeverinBuchser/GitAnalyzer/blob/master/pom.xml). Those executions use the [exec-maven-plugin](https://www.mojohaus.org/exec-maven-plugin/) to run Java executions. Each execution is based on a configuration file, located in the [configs directory of the repo](https://github.com/SeverinBuchser/GitAnalyzer/tree/master/configs). Each config file, except one, is based on the schema mentioned above and concern one project list. To run these configurations do:

```shell
mvn exec:java@{execution_id}
```
where the `execution_id` is the name of the execution, which can be one of the following: "random-asc", "random-desc", "cpp", "go", "java", "java-original", "javascript", "python" or "typescript". There is also one configuration, [the default configuration](https://github.com/SeverinBuchser/GitAnalyzer/blob/master/configs/config.json), which runs the analysis on every project list (in series). If you would like to run the default configuration with every project list, do

```shell
mvn exec:java
```


## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

You still need [Apache Maven](https://maven.apache.org/) to be able to manage the dependencies for the project (see section <a href="#prerequisites">Prerequisites</a>). If you have an IDE like [IntelliJ](https://www.jetbrains.com/idea/) installed, the process of running the application is made much easier while developing. Tests can also be run much more easily.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



## License

Distributed under the MIT License. See [`LICENSE.txt`](https://github.com/SeverinBuchser/GitAnalyzer/blob/master/LICENSE.txt) for more information.

[stars-shield]: https://img.shields.io/github/stars/SeverinBuchser/GitAnalyzer.svg?style=for-the-badge
[stars-url]: https://github.com/SeverinBuchser/GitAnalyzer/stargazers
[issues-shield]: https://img.shields.io/github/issues/SeverinBuchser/GitAnalyzer.svg?style=for-the-badge
[issues-url]: https://github.com/SeverinBuchser/GitAnalyzer/issues
[license-shield]: https://img.shields.io/github/license/SeverinBuchser/GitAnalyzer.svg?style=for-the-badge
[license-url]: https://github.com/SeverinBuchser/GitAnalyzer/blob/master/LICENSE.txt
[latest-release-shield]: https://img.shields.io/github/v/release/SeverinBuchser/GitAnalyzer.svg?display_name=tag&style=for-the-badge
[latest-release-url]: https://github.com/SeverinBuchser/GitAnalyzer/releases/latest
[cc-logomark]: https://github.com/creativecommons/cc-assets/blob/main/logos/cc/logomark.svg
