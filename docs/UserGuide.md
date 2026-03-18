# User Guide

## Introduction

InternTrack is a command-line application that helps users track internship applications.

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
1. Download the latest version of `InternTrack` from [here](https://github.com/AY2526S2-CS2113-W10-1/tp/releases/tag/v1.0).

## Features

Notes about the command format:
* Words in UPPER_CASE are the parameters to be supplied by the user (e.g., in add c/COMPANY, COMPANY is a parameter).
* Items in square brackets are optional (e.g., [d/DEADLINE]).
* Parameters can be in any order (e.g., c/Google r/Intern is the same as r/Intern c/Google).

### 3.1. Add a new internship application: `add`

Adds a new internship application to your tracker. This allows you to log the essential details—Company and Role—and optionally record the application deadline immediately so you never miss a closing date.

Format: `add c/COMPANY r/ROLE [d/DEADLINE] [ct/CONTACT]`

* c/COMPANY: The name of the company (e.g., Google, Meta).
* r/ROLE: The position applied for (e.g., Software Engineer).
* d/DEADLINE: (Optional) The closing date for the application or next task.
    * Format: YYYY-MM-DD
* ct/CONTACT: (Optional) The HR contact/primary recruiter contact for this application.

Examples:
* `add c/Google r/Software Engineer`
    * Adds an application for Google as a Software Engineer with default status Pending.
* `add c/Shopee r/Backend Intern d/2023-11-30 ct/Johns`
    * Adds an application with a specific deadline and through Johns.

## FAQ


## Command Summary

* Add internship application: `add c/COMPANY r/ROLE [d/DEADLINE] [ct/CONTACT]`
