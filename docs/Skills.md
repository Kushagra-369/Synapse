# Skills System

## What is a Skill?

A Skill is a self-contained module that knows how to perform actions for one application or one Android feature.

Each Skill exposes a list of supported actions.

The AI never directly executes app logic.

Instead:

User
↓

AI

↓

Action

↓

Skill

↓

Execution

---

# Skill Structure

Every Skill contains:

- Name
- Description
- Required Permissions
- Supported Actions
- Execution Logic
- Verification Logic

---

# Example

Clock Skill

Actions

- set_alarm
- remove_alarm
- set_timer
- cancel_timer
- start_stopwatch
- stop_stopwatch

---

Gallery Skill

Actions

- search_photo
- open_photo
- search_by_text
- search_by_person
- search_by_location

---

Calendar Skill

Actions

- create_event
- update_event
- delete_event
- today's_events
- weekly_schedule

---

Chrome Skill

Actions

- open_url
- search
- open_bookmark
- open_history

---

YouTube Skill

Actions

- search_video
- play_video
- pause
- resume
- like
- subscribe
- change_speed

---

Maps Skill

Actions

- navigate
- search_place
- nearby_places
- share_location

---

Files Skill

Actions

- search_file
- open_file
- move_file
- delete_file
- rename_file

---

Contacts Skill

Actions

- search_contact
- call_contact
- message_contact
- share_contact

---

Settings Skill

Actions

- wifi
- bluetooth
- flashlight
- brightness
- volume
- dnd

---

# Rules

Every Skill must:

- Define permissions
- Validate input
- Return structured output
- Report success/failure
- Be independently testable

---

# Future

Third-party developers should be able to build Skills using the Synapse SDK.