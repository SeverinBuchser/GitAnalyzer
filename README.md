[![Stargazers][stars-shield]][stars-url][![Issues][issues-shield]][issues-url][![MIT License][license-shield]][license-url][![Latest release][latest-release-shield]][latest-release-url]


<p align="center">
    	<img src="https://raw.githubusercontent.com/SeverinBuchser/GitAnalyzer/master/git-analyzer-logo.svg" alt="git-analyzer-logo" width="20%"/> 
</p>
<h1 align="center">
	Git Analyzer
</h1>
<p><sub align="center" size="0.5">
	<img src="https://mirrors.creativecommons.org/presskit/icons/cc.xlarge.png" alt="cc-logo" width="20" background="white"/> 
	Git Analyzer Logo by <a href="https://github.com/SeverinBuchser/">Severin Buchser</a> is licensed under the <a href="https://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported License</a> / <a href="https://git-scm.com/downloads/logos">The original Git-Logo</a> is remixed into the git-analyzer-logo.
</sub></p>


This project is part of a bachelor-thesis in the [Software Engineering Group](https://seg.inf.unibe.ch/) at the [University of Bern](https://www.unibe.ch/). The project is used for the data-collection and data-analyzing process of the thesis. The thesis focuses on the human intervention in merge conflicts: How often are the used merge resolutions not reproducible by a computer?

This tool collects information about merges of git-projects. The information one can collect is how many conflicting merges, conflicting files and conflicting chunks are in a project as well as how many of the previously stated abstractions can be reproduced by a computer.

# Getting Started

To get a local copy up and running follow these simple example steps.

## Prerequisites

You will need to install [Apache Maven](https://maven.apache.org/) to build the project.

* [Apache Maven Download](https://maven.apache.org/download.cgi)
* [Apache Maven Installation Guide](https://maven.apache.org/install.html)

## Installation

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

## Project Lists

It is important to first understand project lists for the following sections. Project lists are CSV files, with the following structure:

```
# example project list CSV file
name,remoteURL
name_one,remoteURL_one
name_two,remoteURL_two
...
```

The `name` column denotes the name of a project and the `remoteURL` column is the remote repository url, from which the project can be cloned.

## Cloning

There are three clone-subcommands: [clone a project list](#clone), [clone all project lists within a config](#clone-config) and [clone a project list within a config](#clone-project-list). The first one takes single arguments and options and clones a stand-alone project list. The next one clones all project lists of a config and the last one clones one single project list within a config. The cloning of a project in all three commands is the same: There is always a `dir` property, which determines the directory into which the projects will be cloned. Then each project of the list will be cloned into a subdirectory within the `dir`. The subdirectory is named after the project's name, given in the project list.

## Analyzing

There are three analyze-subcommands: [analyze a project list](#analyze), [analyze all project lists within a config](#analyze-config) and [analyze a project list within a config](#analyze-project-list). The first one takes single arguments and options and analyzes a stand-alone project list. The next one analyzes all project lists of a config and the last one analyzes one single project list within a config. The analysis of a project list in all three commands is the same: The projects within a list will be analyzed and the ouput report. All commands have a `list`, an `out` and a `suffix` property. The `list` will provide the name, then the `out` will provide the directory of the output file and the suffix will be appended to the name of the output file with a hyphen. The output is in JSON format. 

## Running

There are two run-subcommands: [run a config](#run-config) and [run a project list within a config](#run-project-list). Both of those commands are based on a config file. The config file determines if the command clones and or analyzes a project list. To better understand how a config interacts with the commands, read the usage help of the individual commands as well as take a look at the JSON-schema of the config: [config.json](https://github.com/SeverinBuchser/GitAnalyzer/blob/master/src/main/resources/config.json).

## Execution

The intended way to execute the tool is to run the "git-analyzer" executable, see [above](#make-executable). If you would choose to create an executable jar yourself, or download the jar, usually with name "GitAnalyzer-2.0.0-jar-with-dependencies.jar", the jar file can be executed:

```shell
java -jar path/to/jar/directory/GitAnalyzer-2.0.0-jar-with-dependencies.jar [..options]
```

Here, the main command `git-analyzer` must not be executed again but rather the [options](#basic-usage) must be supplied. The last way to execute the tool (only predefined executions, intended for development purposes) is described [here](#predefined-executions).

### Basic Usage

```shell
Usage: git-analyzer [-hV] [COMMAND]
Study merge conflict resolution behaviour of Git projects.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  gui      Opens the gui.
  analyze  Runs an analysis of a list of projects.
  clone    Clones a list of projects to one directory. Each project will have
             its own sub-directory which is named after the projects name,
             which is stored inside the list of projects.
  config   Command to manipulate configuration files.
```

#### GUI

The GUI provides a self-explanatory interface for editing and running configs.

```shell
Usage: git-analyzer gui [-hVv] [-c=<config>]
Opens the gui.
  -c, --config=<config>   The config file to open in the gui.
  -h, --help              Show this help message and exit.
  -v, --verbose           Increase verbosity. Specify multiple times to
                            increase (-vvv).
  -V, --version           Print version information and exit.
```

#### Analyze

```shell
Usage: git-analyzer analyze [-hVv] [-d=<dir>] [-o=<out>] [-s=<suffix>] <list>
Runs an analysis of a list of projects.
      <list>              The list of projects to analyze.
  -d, --dir=<dir>         The location of the projects in the list of projects
                            <list>.
  -h, --help              Show this help message and exit.
  -o, --out=<out>         The directory of the output file.
  -s, --suffix=<suffix>   The suffix, appended to the output file.
  -v, --verbose           Increase verbosity. Specify multiple times to
                            increase (-vvv).
  -V, --version           Print version information and exit.
```

#### Clone

```shell
Usage: git-analyzer clone [-hVv] [-d=<dir>] <list>
Clones a list of projects to one directory. Each project will have its own
sub-directory which is named after the projects name, which is stored inside
the list of projects.
      <list>        The list of projects to clone.
  -d, --dir=<dir>   The directory the projects will be cloned to.
  -h, --help        Show this help message and exit.
  -v, --verbose     Increase verbosity. Specify multiple times to increase
                      (-vvv).
  -V, --version     Print version information and exit.
```

#### Config

There is an option to use a JSON config file for the application. The schema is located at [config.json](https://github.com/SeverinBuchser/GitAnalyzer/blob/master/src/main/resources/config.json) and there are some example configurations [here](https://github.com/SeverinBuchser/GitAnalyzer/tree/master/configs).

```shell
Usage: git-analyzer config [-hV] [COMMAND]
Command to manipulate configuration files.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  run           Runs the config. If the clone property is set to true in the
                  config, all project lists will be cloned. If the analyze
                  property is set to true in the config, all project lists will
                  be analyzed. The cloning will happen first.
  analyze       Analyzes the project lists of the config, disregarding the
                  analyze property of the config.
  clone         Clones the project lists of the config, disregarding the clone
                  property of the config.
  show          Prints the config.
  create        Creates a new config and saves it.
  set-clone     Sets the clone property of the config. If true, the config will
                  be cloned if ran.
  set-analyze   Sets the analyze property of the config. If true, the config
                  will be analyzed when ran.
  set-out       Sets the out directory of the config.
  project-list  Command to manipulate specific project lists within a config
                  file.
```

<a name="run-config"></a>

##### Run

```shell
Usage: git-analyzer config run [-hVv] <config>
Runs the config. If the clone property is set to true in the config, all
project lists will be cloned. If the analyze property is set to true in the
config, all project lists will be analyzed. The cloning will happen first.
      <config>    The path of the config.
  -h, --help      Show this help message and exit.
  -v, --verbose   Increase verbosity. Specify multiple times to increase (-vvv).
  -V, --version   Print version information and exit.
```

<a name="analyze-config"></a>

##### Analyze

```shell
Usage: git-analyzer config analyze [-hVv] <config>
Analyzes the project lists of the config, disregarding the analyze property of
the config.
      <config>    The path of the config.
  -h, --help      Show this help message and exit.
  -v, --verbose   Increase verbosity. Specify multiple times to increase (-vvv).
  -V, --version   Print version information and exit.
```

<a name="clone-config"></a>

##### Clone

```shell
Usage: git-analyzer config clone [-hVv] <config>
Clones the project lists of the config, disregarding the clone property of the
config.
      <config>    The path of the config.
  -h, --help      Show this help message and exit.
  -v, --verbose   Increase verbosity. Specify multiple times to increase (-vvv).
  -V, --version   Print version information and exit.
```

##### Show

```shell
Usage: git-analyzer config show [-hVv] <config>
Prints the config.
      <config>    The path of the config.
  -h, --help      Show this help message and exit.
  -v, --verbose   Increase verbosity. Specify multiple times to increase (-vvv).
  -V, --version   Print version information and exit.
```

##### Create

```shell
Usage: git-analyzer config create [-achVv] [-o=<out>] <config>
Creates a new config and saves it.
      <config>      The path of the config.
  -a, --analyze
  -c, --clone
  -h, --help        Show this help message and exit.
  -o, --out=<out>
  -v, --verbose     Increase verbosity. Specify multiple times to increase
                      (-vvv).
  -V, --version     Print version information and exit.
```

##### Set-Clone

```shell
Usage: git-analyzer config set-clone [-hVv] <config> <clone>
Sets the clone property of the config. If true, the config will be cloned if
ran.
      <config>    The path of the config.
      <clone>
  -h, --help      Show this help message and exit.
  -v, --verbose   Increase verbosity. Specify multiple times to increase (-vvv).
  -V, --version   Print version information and exit.
```

##### Set-Out

```shell
Usage: git-analyzer config set-out [-hVv] <config> <out>
Sets the out directory of the config.
      <config>    The path of the config.
      <out>
  -h, --help      Show this help message and exit.
  -v, --verbose   Increase verbosity. Specify multiple times to increase (-vvv).
  -V, --version   Print version information and exit.
```

##### Project-List

```shell
Usage: git-analyzer config project-list [-hV] [COMMAND]
Command to manipulate specific project lists within a config file.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  run         Runs a single project list within a config. If the clone property
                is set to true in the config, the project list will be cloned.
                If the analyze property is set to true in the config, all the
                project list will be analyzed. The cloning will happen first.
                The skip property of the project list is disregarded.
  clone       Clones a single project list of the config, disregarding the
                clone property of the config. The skip property of the project
                list is disregarded.
  analyze     Analyzes a single project list of the config, disregarding the
                analyze property of the config.The skip property of the project
                list is disregarded.
  show        Prints the project list.
  add         Adds a new project list to the config.
  remove      Removes a project list from the config.
  set-list    Sets the project list (list) property of the project list.
  set-dir     Sets the project directory (dir) property of the project list.
  set-suffix  Sets the suffix property of the project list. The suffix is
                appended with a hyphen to the output file.
  set-skip    Sets the skip property of the project list. If the whole config
                is cloned or analyzed, a project list with a true skip property
                will be skipped.
```

<a name="run-project-list"></a>

###### Run

```shell
Usage: git-analyzer config project-list run [-hVv] <config> <project-list>
Runs a single project list within a config. If the clone property is set to
true in the config, the project list will be cloned. If the analyze property is
set to true in the config, all the project list will be analyzed. The cloning
will happen first. The skip property of the project list is disregarded.
      <config>         The path of the config.
      <project-list>   The name of the project list.
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

<a name="clone-project-list"></a>

###### Clone

```shell
Usage: git-analyzer config project-list clone [-hVv] <config> <project-list>
Clones a single project list of the config, disregarding the clone property of
the config. The skip property of the project list is disregarded.
      <config>         The path of the config.
      <project-list>   The name of the project list.
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

<a name="analyze-project-list"></a>

###### Analyze

```shell
Usage: git-analyzer config project-list analyze [-hVv] <config> <project-list>
Analyzes a single project list of the config, disregarding the analyze property
of the config.The skip property of the project list is disregarded.
      <config>         The path of the config.
      <project-list>   The name of the project list.
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

###### Show

```shell
Usage: git-analyzer config project-list show [-hVv] <config> <project-list>
Prints the project list.
      <config>         The path of the config.
      <project-list>   The name of the project list.
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

###### Add

```shell
Usage: git-analyzer config project-list add [-hsVv] [-d=<dir>] [-sx=<suffix>]
       <config> <project-list>
Adds a new project list to the config.
      <config>         The path of the config.
      <project-list>   The name of the project list.
  -d, --dir=<dir>
  -h, --help           Show this help message and exit.
  -s, --skip
      -sx, --suffix=<suffix>

  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

###### Remove

```shell
Usage: git-analyzer config project-list remove [-hVv] <config> <project-list>
Removes a project list from the config.
      <config>         The path of the config.
      <project-list>   The name of the project list.
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

###### Set-List

```shell
Usage: git-analyzer config project-list set-list [-hVv] <config> <project-list>
       <list>
Sets the project list (list) property of the project list.
      <config>         The path of the config.
      <project-list>   The name of the project list.
      <list>
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

###### Set-Dir

```shell
Usage: git-analyzer config project-list set-dir [-hVv] <config> <project-list>
       <dir>
Sets the project directory (dir) property of the project list.
      <config>         The path of the config.
      <project-list>   The name of the project list.
      <dir>
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

###### Set-Suffix

```shell
Usage: git-analyzer config project-list set-suffix [-hVv] <config>
       <project-list> <suffix>
Sets the suffix property of the project list. The suffix is appended with a
hyphen to the output file.
      <config>         The path of the config.
      <project-list>   The name of the project list.
      <suffix>
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

###### Set-Skip

```shell
Usage: git-analyzer config project-list set-skip [-hVv] <config> <project-list>
       <skip>
Sets the skip property of the project list. If the whole config is cloned or
analyzed, a project list with a true skip property will be skipped.
      <config>         The path of the config.
      <project-list>   The name of the project list.
      <skip>
  -h, --help           Show this help message and exit.
  -v, --verbose        Increase verbosity. Specify multiple times to increase
                         (-vvv).
  -V, --version        Print version information and exit.
```

### Predefined Executions

Predefined executables are already provided in the maven configuration, [pom.xml](https://github.com/SeverinBuchser/GitAnalyzer/blob/master/pom.xml). Those executions use the [exec-maven-plugin](https://www.mojohaus.org/exec-maven-plugin/) to run Java executions. Each execution is based on a configuration file, located in the [configs directory of the repo](https://github.com/SeverinBuchser/GitAnalyzer/tree/master/configs). Each config file, except one, is based on the schema mentioned above and concerns one project list.

To execute the predefined executions do

```shell
mvn exec:java@{execution_id}
```
where the `execution_id` is the name of the execution, which can be one of the following: "random-asc", "random-desc", "cpp", "go", "java", "java-original", "javascript", "python", "typescript", "simple" or "gui". The latter, "gui", will execute the `git-analyzer gui -c=<config>` command. The predefined executions for the config files run the command `git-analyzer config run <config>`. There is also one configuration, [the default configuration](https://github.com/SeverinBuchser/GitAnalyzer/blob/master/configs/config.json), which runs the analysis on every project list (in series). If you would like to run the default configuration with every project list, do

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
