CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE students (
    id UUID PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    birth_date DATE NOT NULL,
    gender VARCHAR(30) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(160),

    address_zip_code VARCHAR(10) NOT NULL,
    address_street VARCHAR(180) NOT NULL,
    address_number VARCHAR(20) NOT NULL,
    address_complement VARCHAR(100),
    address_neighborhood VARCHAR(120) NOT NULL,
    address_city VARCHAR(120) NOT NULL,
    address_state VARCHAR(2) NOT NULL,

    emergency_contact_name VARCHAR(150) NOT NULL,
    emergency_contact_relationship VARCHAR(60) NOT NULL,
    emergency_contact_phone VARCHAR(20) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_students_status CHECK (status IN ('NEW', 'ACTIVE', 'INACTIVE'))
);

CREATE TABLE student_anamneses (
    id UUID PRIMARY KEY,
    student_id UUID NOT NULL REFERENCES students(id) ON DELETE CASCADE,

    medical_history JSONB NOT NULL,
    physical_activity_history JSONB NOT NULL,

    primary_goal VARCHAR(40) NOT NULL,
    primary_goal_other VARCHAR(150),
    secondary_goal VARCHAR(150),
    focus_body_area VARCHAR(150),
    weekly_availability VARCHAR(255) NOT NULL,
    intended_weekly_frequency SMALLINT NOT NULL,

    weight_kg NUMERIC(5,2),
    height_cm NUMERIC(5,2),
    body_fat_percentage NUMERIC(5,2),
    muscle_mass_kg NUMERIC(5,2),
    blood_pressure VARCHAR(20),
    resting_heart_rate SMALLINT,
    professional_notes TEXT,

    has_health_alerts BOOLEAN NOT NULL DEFAULT FALSE,
    health_alerts_count SMALLINT NOT NULL DEFAULT 0,

    terms_version VARCHAR(20) NOT NULL DEFAULT 'v1',
    terms_accepted BOOLEAN NOT NULL,
    lgpd_consent_accepted BOOLEAN NOT NULL,
    signer_name VARCHAR(150) NOT NULL,
    signature_image TEXT NOT NULL,
    signed_at TIMESTAMPTZ NOT NULL,
    signer_ip VARCHAR(64),
    signer_user_agent VARCHAR(300),

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_anamneses_terms CHECK (terms_accepted = TRUE AND lgpd_consent_accepted = TRUE),
    CONSTRAINT chk_anamneses_freq CHECK (intended_weekly_frequency BETWEEN 1 AND 7)
);

CREATE INDEX idx_students_status ON students (status);
CREATE INDEX idx_students_full_name ON students (LOWER(full_name));
CREATE INDEX idx_students_created_at ON students (created_at DESC);
CREATE INDEX idx_anamneses_student_created ON student_anamneses (student_id, created_at DESC);
CREATE INDEX idx_anamneses_health_alerts ON student_anamneses (has_health_alerts);
