# Initial setup and testing prompt

## Validating installation

YOu might look at the git projects included on this machine to see if we are ready for the other tasks. This includes Antigravity, git, the projects and scripts, node.js, Java, and Cameo. Note that I have started both test harnesses for you at `http://localhost:8765/run` (Groovy) and `http://localhost:8764/run` (Python). The Groovy installed is properly configured in the CATIA Magic/Cameo Automation plugin.

In addition to the tools listed above, please ensure to validate:

- **Antigravity**: Ensure Antigravity is installed and running. Verify that the MCP servers are properly configured and accessible.
- **git**: Ensure git is installed and running. Verify that the MCP servers are properly configured and accessible.
- **node.js**: Ensure node.js is installed and running. Verify that the MCP servers are properly configured and accessible.
- **Java**: Ensure Java is installed and running. Verify that the MCP servers are properly configured and accessible. Note that this installaion is usually managed  by the CATIA Magic (Cameo) installer in the Applications directory.
- **Python and Dependencies**: Validate Python installation and required `pip` packages.
- **Test Harness Accessibility**: Verify that the Python and Groovy test harnesses at ports 8764 and 8765 are actually running and reachable.
- **Cameo License**: Ensure Cameo has an active license checked out (to prevent headless execution from hanging on UI popups).
- **Groovy Installation**: Installation of Groovy is required, because the Groovy embedded in Cameo does not support some of the latest features of the language. This means that the `<install dir>/plugins/com.nomagic.magicdraw.automaton/plugin.xml` points to the local installation of Groovy and that all jar files in the installation are listed in the plugin.xml file. Here is an example of the plugin.xml file: "MBSE-AI-Lab/Example Automation Plugin Config file/plugin.xml"
- **Environment Variables**: Verify essential variables (e.g., `JAVA_HOME`, Cameo installation directories).
- **Build Tools**: Verify if `Maven` or `Gradle` are required and installed for compiling any plugins.

If anything is missing, please create a todo list, and I will address it.

## Validating prompt execution

Ok, after addressing any installation issues, here is the first prompt to verify your capability to develop, test, and validate.

Labs\Lab1\promptSwimmingRobotModel.md
