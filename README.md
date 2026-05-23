# Supabase Notes (JavaFX + JDBC)

A minimal, beginner-friendly JavaFX + Maven app that does CRUD for notes stored in a Supabase Postgres database.

## Requirements

- JDK 24
- Maven (or use the included wrapper)
- Supabase project (Postgres database)

## Tech

- JavaFX 21.0.6 (FXML + Controllers)
- Maven
- JDBC (PostgreSQL driver)
- `.env` config via `dotenv-java`

## Project Idea

You have a simple Notes app:

- Main window shows a table of notes (title + created date)
- Clicking a row shows the note details at the bottom
- A second window (modal) lets you Add/Edit a note
- Delete works from the main window

## Database Setup (Supabase)

1. Create a Supabase project.
2. Go to **SQL Editor** and run the script in `supabase_notes.sql`.

This creates a single table:

- `notes(id uuid, title text, content text, created_at timestamptz)`

## `.env` Setup

1. Copy `.env.example` to `.env`
2. Fill in your real values.

Minimum keys:

- `SUPABASE_DB_HOST`
- `SUPABASE_DB_PORT`
- `SUPABASE_DB_NAME`
- `SUPABASE_DB_USER`
- `SUPABASE_DB_PASSWORD`
- `SUPABASE_DB_SSLMODE` (or `SUPABASE_DB_SSL_MODE`)

### Recommended (Transaction Pooler)

Some networks block/limit direct database connections (`db.<project-ref>.supabase.co:5432`), especially if it’s IPv6-only.
If you see connection errors, use the Supabase **Transaction Pooler** connection info:

- Host: something like `aws-1-<region>.pooler.supabase.com`
- Port: usually `6543`
- User: usually `postgres.<your-project-ref>`
- DB: usually `postgres`

Verify connectivity (PowerShell):

`Test-NetConnection aws-1-<region>.pooler.supabase.com -Port 6543`

## Run

From the project root:

- `./mvnw javafx:run`

## OOP Structure (simple)

- `com.example.oopworkshopv2.model` — data classes (`Note`)
- `com.example.oopworkshopv2.dao` — SQL/JDBC (`NoteDao`)
- `com.example.oopworkshopv2.service` — app logic (`NoteService`)
- `com.example.oopworkshopv2.controller` — JavaFX controllers (UI logic)
- `com.example.oopworkshopv2.db` — DB connection helper (`Db`)
- `com.example.oopworkshopv2.util` — `.env` + UI helpers (`Env`, `Alerts`)

## Troubleshooting

- **Notes list is blank**: ensure you ran `supabase_notes.sql` and your `.env` values are correct.
- **`UnknownHostException` / connection attempt failed**: your network/DNS may block direct DB connections. Use the **Transaction Pooler** host/port.
- **SSL issues**: keep `SUPABASE_DB_SSLMODE=require` (Supabase typically requires SSL).

