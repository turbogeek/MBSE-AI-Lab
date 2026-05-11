# Tutorial One: Notes

Title: Using Cameo API with MCP to create a SysMLv2 model for a automated pool cleaning robot.

## Introduction

This tutorial will guide you through the process of using Cameo API with MCP to create a SysMLv2 model for a automated pool cleaning robot.
But why?

## The Old Way: Programming manually with much pain and suffering

Programming using the JavaDocs and no context from the environment. This is often an interactive process where the user is running the script in the Cameo environment. It is a process full of dead ends, red herrings, and the occasional gem of information. You need to be a good programmer or a good guesser, and definitly patient and lucky.
I have written a fair amount of Java and Groovy code for the Cameo API, and I have found that it is a real pain in the posterior to develop scripts this way. There are a few issues:

- The API is vast and partially deceitful.
-- There are seveal packages that have overalapping functionality and it is not always clear which one to use.
-- You need to understant the UML meta model really well to use it
-- You need to understand the additional standard, like SysML or UAF, and how it relates to the meta model and the API.

- The documentation, while thorough,  doesn't always provide the context or the best practices for using the API. It's more of a reference than a tutorial.
-- There are basic examples provided, just nothing about 'your' use case. So you have to figure out how to apply it to your problem.

- Even when you are a good programmer, this is not your typical programming task.
-- There is no good unit testing environment provided
-- It is difficult to debug scripts
-- Using Java in a plugin is worse because of the integration with the IDE and turnaround time for each iteration.
-- You don't have a real IDE to help you code because you are a modeler that needs a bit of script to do something you oryour boss thought would be 'easy' or 'quick'.

## Back to the goal

- Need to create a script that does something the tool does not do well.
- Should be somewhat unique and useful,illing a need.
- Should not become a second job.
- Could be done by an intern, but we can't afford an intern.
-- Wait! Is the AI a low-paid intern?  
-- Could we give this tutorial to the intern anf a cheap LLM AI?
- That's why we are here...
-- ***INTERN OPTIONAL***

## The New Way: Using an LLM that uses anMCP client with an Agent to help... and with a smidgen of pain, but with less suffering

<INSERT an overview that explains the MCP and Agent setup process - how Claude help with the initial prompts and the iteration process to get to the final result.  Mention the LLM is not perfect and the user must intervene from time to time.  Make it narrative and fun.

## Test Harness

There are script in the folder, "test harness", which will provide the LLM with a way to run and test its code in the Cameo environment. The LLM can run scripts using this harness, and the harness will report the results back to the LLM. This provides a way to smoke test a sctipt. We can also instruct the LLM to write good logs and even create screen shots to allow it to debug more effectively.

## Let's setup and then have the LLM do our bidding

### The "Swimming Robot" Automation Prompt


```text
Act as a SysMLv2 modeling expert for the CATIA Magic Open API with expertise in theGroovy Language, Java, the SysMLv2 standard, and the Cameo API. Your task is to develop, test, and validate a Groovy script that will show a Satisfy matrix with the given requirements and architecture in this file: "TutorialOne\TutorialTwo\requirements.md".


### Execution Requirements:
- Wrap all model changes in a SessionManager transaction.
- Use ElementsFactory from the selected Namespace in the containment tree.
- Load and use the SysMLv2Logger utility with a dedicated log file at "E:\_Documents\git\TutorialForCatiaMagicApiMCP\logs\SwimmingRobot.log".
- DO NOT use GStrings (e.g., "${var}"); use string concatenation or .toString().
- Deploy and run the script via the Cameo Test Harness at http://localhost:8765/run.

### Validation Loop:
1. Generate the script and save it to "C:\Users\DBR2\Desktop\SwimmingRobot\SwimmingRobot.groovy".
2. Trigger the /run endpoint on the test harness.
3. Check the /status and tail the /log.
4. If compilation or runtime errors occur (e.g., "unable to resolve class"), search the Javadoc via the MCP, fix the script, and re-run.
5. Report the final containment tree structure in your summary.
```

> [!WARNING]
> Remember, the LLM is not perfect and will make mistakes.  Be prepared to correct it and guide it as needed.  It is not a magic bullet.
>

## A quote

There are known knowns. These are things we know that we know. There are known unknowns. That is to say, there are things that we know we don't know. But there are also unknown unknowns. There are things we don't know we don't know.

Donald Rumsfeld


> [!IMPORTANT}
> When the LLM makes mistakes (or you do):
> -Change the requirements and or the plan 
> -Change the  skill/agent files when there are hallucination  
>


### iterate to complete and test the script


## Project Setup

- Repository: [TutorialForCatiaMagicApiMCP](file:///e:/_Documents/git/TutorialForCatiaMagicApiMCP)
- MCP Server Config: `MCP4MagicAPI`

## Key Concepts

- **Cameo API**: Java-based API for MagicDraw/Cameo.
- **MCP Server**: Bridging LLMs to local development tools.
- **Tutorial Content**: (Drafting in progress...)

## Presentation Ideas

- Introduction to the MCP Server.
- Why we need it for Cameo API.
- Live demonstration of script generation.
- Error handling and session management.

## TODOs

- [ ] Finalize tutorial script and correct any markown errors or spelling mistakes.
- [ ] Export HTML presentation to PowerPoint (using Pandoc).
- [ ] Test with local Cameo instance.
- [ ] Improve the script and presentation.
- [ ] Add screenshots and samples of the scripts along the way in the tutorial steps.
- [ ] Add summary and lessons learned to the README.
- [ ] Repeat the process to ensure the tutial is bullet proof and understandable.
