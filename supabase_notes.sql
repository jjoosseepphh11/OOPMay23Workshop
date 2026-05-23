-- Run this in Supabase SQL Editor

create extension if not exists pgcrypto;

create table if not exists notes (
    id uuid primary key default gen_random_uuid(),
    title text not null,
    content text not null default '',
    created_at timestamptz not null default now()
);
