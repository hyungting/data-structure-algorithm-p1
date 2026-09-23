# AI Use Disclosure

## AI tool used

I used OpenAI gpt-5.6-terra as a conversational coding assistant. I did not use any
other AI tools for this project.

## How I used it

I asked AI to:

- explain the assignment requirements, make plans for the assignment
- explain Java syntax and how to run JUnit test
- review my proposed defects, root causes, and fixes after I read the provided code
- review my proposed test cases and suggest boundary and adversarial test ideas that I might have overlooked
- proofread and clarify the English in my defect report and analysis
- review if I placed `OpCounter` in reasonable locations
- write the Part C measurement bash script based on my instructions
- write plotting functions based on my instructions
- assess whether my observations and time-complexity conclusions were reasonable
- draft `AI_USE.md` based on our conversation

## My changes and verification

I read the supplied implementation and made the final decisions about each
defect, test, repair, and analysis myself. I adapted rather than copied suggestions:

- I identified the defects, root causes, and fix methods myself, and challenged suggestions that seemed unreasonable.
- I checked whether the specification and AI-proposed tasks were aligned and found several mismatches.
- I wrote and edited the JUnit tests to match the published API.
- I iteratively modified the Part C plots for the layout, variable use, and dimensions, so they would support my own conclusions.
- I rejected unsupported conclusions, changed the exponentiation analysis to distinguish control-flow levels from digit-level work, and chose the final wording and figures myself.