# Synapse Architecture

## Vision

Synapse is an AI-powered Android action platform that enables users to control their phone using natural language.

Instead of navigating apps manually, users describe what they want, and Synapse understands, plans, and executes the required actions.

---

# Core Principle

Understand → Plan → Execute → Verify → Respond

---

# High-Level Flow

User
↓
Voice / Text
↓
AI Intent Engine
↓
Planner
↓
Action Dispatcher
↓
Skill
↓
Android APIs / Accessibility
↓
Application
↓
Verification
↓
Response

---

# Core Components

## 1. Input Layer

Responsible for receiving user input.

Supports:

- Text
- Voice
- Future Wearables

---

## 2. Intent Engine

Converts natural language into structured intent.

Example

Input:

Set an alarm for 6 AM tomorrow.

Output

Intent:
set_alarm

Parameters:

time: 6:00 AM
date: tomorrow

---

## 3. Planner

Breaks complex requests into multiple actions.

Example:

"Wake me up at 6, then remind me to call Mom at 8."

↓

Task 1

Create Alarm

↓

Task 2

Create Reminder

---

## 4. Action Dispatcher

Chooses which Skill should execute an action.

Example

set_alarm

↓

Clock Skill

---

## 5. Skills

Every application or system feature is represented as a Skill.

Examples:

Clock

Calendar

Gallery

Files

Chrome

YouTube

WhatsApp

Maps

Camera

Settings

---

## 6. Android Layer

Executes actions using:

- Official Android APIs
- Intents
- Accessibility Services (only when required)

---

## 7. Verification Layer

Checks whether the requested action completed successfully.

Example

Alarm Created

↓

Success

---

## 8. Response Layer

Returns a human-friendly response.

Example

"Alarm has been set for tomorrow at 6 AM."

---

# Design Principles

- API first
- Skills are modular
- AI never directly manipulates apps if an official API exists
- Accessibility is the last option
- Every action must be verifiable
- Offline-first whenever possible
- Privacy-first