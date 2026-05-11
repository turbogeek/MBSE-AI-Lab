---
marp: true
theme: default
class: lead
paginate: true
backgroundColor: #f8fafc
color: #1e293b
style: |
  h1 { color: #005386; font-family: 'Outfit', sans-serif; font-weight: 800; }
  h2 { color: #00A3E0; font-family: 'Outfit', sans-serif; font-weight: 700; }
  .pain { color: #ef4444; font-weight: 800; }
  .joy { color: #10b981; font-weight: 800; }
  code { background-color: #f1f5f9; color: #0f172a; border: 1px solid #cbd5e1; }
  img { box-shadow: 0 10px 25px rgba(0,0,0,0.15); border-radius: 8px; border: 1px solid #cbd5e1; }
---

# Tutorial Two
## SysMLv2 Satisfy Matrix GUI
From simple scripts to powerful, interactive tooling.

---

## <span class="joy">Why Build Tool Extensions?</span>
We often need more control, customized views, and specific workflows than the base tool offers.

- **Custom Visualizations:** Fit the views to your specific organizational standards.
- **Implied Relationships:** Show data the tool knows, but doesn't explicitly draw.
- **Integration:** Bridge Cameo with Jira, DOORS, or custom reporting.
- **AI Synergy:** Integrate AI directly into the tool to do more than its developers intended.

---

## Matrix Architecture
What are we building?

- **UI Framework:** Standard Java Swing `JDialog` with custom `Graphics2D` drawing.
- **Data Mining:** Deep scanning of `RequirementUsage` and `SatisfyRequirementUsage` inside a namespace.
- **Visual Indicators:** Direct Satisfy (Green Arrow) vs Implied Satisfy (Blue Dashed Arrow).
- **Interactivity:** Double-clicking a cell automatically selects the relevant item in the containment tree.

---

## <span class="pain">Direct vs Implied Satisfy</span>
A crucial systems engineering distinction.

**Direct Satisfy:** The element explicitly owns a `SatisfyRequirementUsage` pointing to the Requirement.

**Implied Satisfy:** The element *owns* (or is a parent of) a sub-feature that directly satisfies a requirement. If a component's sub-part meets the requirement, the component itself inherently implies satisfaction.

```text
if (A owns B) AND (B satisfies R)
then -> (A implied_satisfies R)
```

---

## Environment Setup
Before running the script, load the test model:

1. Create a new **SysMLv2 Project** in Cameo / MagicDraw.
2. Import the provided `Test Model/Drone.sysml` file.
3. Select the `Drone` package in the containment tree.
4. Run the Satisfy Matrix script!

![height:300](Example%20Matrix%20Dialog/ExampleMatrixDialog.png)

---

## The Resulting Dialog
Our custom Graphic2D Satisfy Matrix rendered inside Cameo.

![height:450](Screen%20Shots%20for%20Tutorial%202/FinalDialogTest.png)

---

## Key Highlights

- **Graphics2D over JTable:** We used custom rendering to achieve beautiful 45-degree angled column headers.
- **Dynamic Toggles:** "Show Implied" dynamically recalculates the tree and repaints instantly.
- **No Mutation:** Unlike Tutorial One, this script doesn't alter the model—it just reads it safely.

---

## The Exact Prompt Used

```markdown
Act as a SysMLv2 modeling expert for the CATIA Magic Open API with expertise in 
the Groovy Language, Java, the SysMLv2 standard, and the Cameo API...

- Wrap all model changes in a SessionManager transaction.
- Create a dedicated script logger targeting `SatisfyMatrix.log`.
- **CRITICAL:** Event handlers and rendering methods (`paintComponent`) execute 
  on the AWT Event Dispatch Thread. Any uncaught exceptions will be intercepted 
  by MagicDraw... You MUST wrap the entire bodies of these methods in a 
  `try-catch (Throwable t)` block and log errors!
```

---

## <span class="pain">When Exceptions Escape</span>
If an error occurs inside a GUI thread, MagicDraw intercepts it and displays the Internal Errors dialog.

![height:350](Example%20Matrix%20Dialog/ErrorNotCaughtByScript.png)

---

## Starting the MCP Server
To give your AI access to the Cameo Open API MCP server, you must configure your AI client to launch it automatically.

**For Claude Desktop:**
Edit your `claude_desktop_config.json`:
```json
"mcpServers": {
  "cameo-api": {
    "command": "node",
    "args": ["E:/_Documents/git/TutorialForCatiaMagicApiMCP/MCP4MagicAPI/build/index.js"]
  }
}
```

---

## Alternative: Command Prompt Startup
If you want to test the server without an AI client, or run it for an agent that connects via standard input/output over a terminal, you can start it directly from the command prompt.

**Run the MCP Inspector (Interactive testing):**
```cmd
cd E:\_Documents\git\TutorialForCatiaMagicApiMCP\MCP4MagicAPI
npx @modelcontextprotocol/inspector node build/index.js
```
*This opens a local web interface to manually test the API tools!*

---

# The Sky is the Limit
Now that you can read, parse, and visualize the SysMLv2 model...

## What will you build next?
