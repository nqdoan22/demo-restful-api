-- Sample data for API clients
-- This script creates sample clients for demonstration

-- Note: For production, API keys should be generated securely using ApiClientService.generateApiKey()

CREATE TABLE IF NOT EXISTS api_client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_name VARCHAR(100) NOT NULL UNIQUE,
    api_key VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL,
    last_used_at DATETIME,
    request_count BIGINT NOT NULL DEFAULT 0,
    contact_email VARCHAR(100),
    client_type VARCHAR(50)
);

-- Insert sample internal client
INSERT INTO api_client (client_name, api_key, description, status, created_at, request_count, contact_email, client_type)
VALUES
    ('Internal Mobile App', 'abc123xyz456internal789', 'Mobile application for internal use', 'ACTIVE', NOW(), 0, 'mobile@company.com', 'INTERNAL'),
    ('Internal Web Portal', 'def456uvw789web123portal', 'Web portal for company employees', 'ACTIVE', NOW(), 0, 'web@company.com', 'INTERNAL'),
    ('Internal Admin System', 'ghi789rst012admin345sys', 'Administrative system for company', 'ACTIVE', NOW(), 0, 'admin@company.com', 'INTERNAL');

-- Insert sample external clients (under 10 as per requirements)
INSERT INTO api_client (client_name, api_key, description, status, created_at, request_count, contact_email, client_type)
VALUES
    ('Partner Company A', 'jkl012mno345partner678A', 'External partner company A', 'ACTIVE', NOW(), 0, 'partner-a@external.com', 'EXTERNAL'),
    ('Partner Company B', 'pqr345stu678partner901B', 'External partner company B', 'ACTIVE', NOW(), 0, 'partner-b@external.com', 'EXTERNAL'),
    ('Demo Client 1', 'vwx678yza901demo234demo1', 'Demo client for testing', 'ACTIVE', NOW(), 0, 'demo1@test.com', 'EXTERNAL'),
    ('Demo Client 2', 'bcd012efg345demo567demo2', 'Demo client for testing', 'ACTIVE', NOW(), 0, 'demo2@test.com', 'EXTERNAL'),
    ('Inactive Client', 'hij678klm901inactive234', 'Inactive client for testing', 'INACTIVE', NOW(), 0, 'inactive@test.com', 'EXTERNAL');

