--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0 (Debian 16.0-1.pgdg120+1)
-- Dumped by pg_dump version 16.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: deal; Type: SCHEMA; Schema: -; Owner: admin
--

CREATE SCHEMA deal;


ALTER SCHEMA deal OWNER TO admin;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: application; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.application (
                                     application_id integer NOT NULL,
                                     client_id integer NOT NULL,
                                     credit_id integer NOT NULL,
                                     created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                     applied_offer jsonb,
                                     signed timestamp without time zone,
                                     ses_code integer
);


ALTER TABLE deal.application OWNER TO admin;

--
-- Name: application_application_id_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.application_application_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.application_application_id_seq OWNER TO admin;

--
-- Name: application_application_id_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.application_application_id_seq OWNED BY deal.application.application_id;


--
-- Name: application_status; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.application_status (
                                            application_status_id integer NOT NULL,
                                            status_type character varying NOT NULL
);


ALTER TABLE deal.application_status OWNER TO admin;

--
-- Name: application_status_application_status_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.application_status_application_status_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.application_status_application_status_seq OWNER TO admin;

--
-- Name: application_status_application_status_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.application_status_application_status_seq OWNED BY deal.application_status.application_status_id;


--
-- Name: change_type; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.change_type (
                                     change_type_id integer NOT NULL,
                                     change_type character varying NOT NULL
);


ALTER TABLE deal.change_type OWNER TO admin;

--
-- Name: change_type_change_type_id_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.change_type_change_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.change_type_change_type_id_seq OWNER TO admin;

--
-- Name: change_type_change_type_id_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.change_type_change_type_id_seq OWNED BY deal.change_type.change_type_id;


--
-- Name: client_pk_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.client_pk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.client_pk_seq OWNER TO admin;

--
-- Name: client; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.client (
                                client_id integer DEFAULT nextval('deal.client_pk_seq'::regclass) NOT NULL,
                                last_name character varying NOT NULL,
                                first_name character varying NOT NULL,
                                middle_name character varying,
                                birth_date date NOT NULL,
                                email character varying NOT NULL,
                                gender_id integer NOT NULL,
                                marital_status integer NOT NULL,
                                dependent_amount integer,
                                account character varying
);


ALTER TABLE deal.client OWNER TO admin;

--
-- Name: COLUMN client.birth_date; Type: COMMENT; Schema: deal; Owner: admin
--

COMMENT ON COLUMN deal.client.birth_date IS 'datebirth of the client';


--
-- Name: COLUMN client.email; Type: COMMENT; Schema: deal; Owner: admin
--

COMMENT ON COLUMN deal.client.email IS 'client contact email';


--
-- Name: credit; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.credit (
                                credit_id integer NOT NULL,
                                term integer NOT NULL,
                                monthly_payment numeric NOT NULL,
                                rate numeric NOT NULL,
                                psk numeric,
                                with_insurance boolean DEFAULT false NOT NULL,
                                salary_client boolean DEFAULT false NOT NULL,
                                credit_status integer NOT NULL
);


ALTER TABLE deal.credit OWNER TO admin;

--
-- Name: credit_credit_id_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.credit_credit_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.credit_credit_id_seq OWNER TO admin;

--
-- Name: credit_credit_id_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.credit_credit_id_seq OWNED BY deal.credit.credit_id;


--
-- Name: credit_status_id_fk_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.credit_status_id_fk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.credit_status_id_fk_seq OWNER TO admin;

--
-- Name: credit_status; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.credit_status (
                                       credit_status_id integer DEFAULT nextval('deal.credit_status_id_fk_seq'::regclass) NOT NULL,
                                       credit_status_type character varying DEFAULT 'issued'::character varying NOT NULL
);


ALTER TABLE deal.credit_status OWNER TO admin;

--
-- Name: employment; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.employment (
                                    employment_id integer NOT NULL,
                                    employment_data jsonb NOT NULL,
                                    client_id integer NOT NULL
);


ALTER TABLE deal.employment OWNER TO admin;

--
-- Name: employment_employment_id_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.employment_employment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.employment_employment_id_seq OWNER TO admin;

--
-- Name: employment_employment_id_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.employment_employment_id_seq OWNED BY deal.employment.employment_id;


--
-- Name: gender_type; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.gender_type (
                                     gender_type_id integer NOT NULL,
                                     gender character varying NOT NULL
);


ALTER TABLE deal.gender_type OWNER TO admin;

--
-- Name: gender_type_gender_type_id_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.gender_type_gender_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.gender_type_gender_type_id_seq OWNER TO admin;

--
-- Name: gender_type_gender_type_id_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.gender_type_gender_type_id_seq OWNED BY deal.gender_type.gender_type_id;


--
-- Name: log_pk_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.log_pk_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.log_pk_seq OWNER TO admin;

--
-- Name: log; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.log (
                             id integer DEFAULT nextval('deal.log_pk_seq'::regclass) NOT NULL,
                             message text,
                             "timestamp" timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE deal.log OWNER TO admin;

--
-- Name: marital_status_type; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.marital_status_type (
                                             marital_status_id integer NOT NULL,
                                             status character varying NOT NULL
);


ALTER TABLE deal.marital_status_type OWNER TO admin;

--
-- Name: marital_status_type_marital_status_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.marital_status_type_marital_status_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.marital_status_type_marital_status_seq OWNER TO admin;

--
-- Name: marital_status_type_marital_status_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.marital_status_type_marital_status_seq OWNED BY deal.marital_status_type.marital_status_id;


--
-- Name: passport; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.passport (
                                  passport_id integer NOT NULL,
                                  passport_data jsonb NOT NULL,
                                  client_id integer NOT NULL
);


ALTER TABLE deal.passport OWNER TO admin;

--
-- Name: passport_passport_id_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.passport_passport_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.passport_passport_id_seq OWNER TO admin;

--
-- Name: passport_passport_id_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.passport_passport_id_seq OWNED BY deal.passport.passport_id;


--
-- Name: payment_schedule; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.payment_schedule (
                                          payment_schedule_id integer NOT NULL,
                                          credit_id integer NOT NULL,
                                          schedule_data jsonb NOT NULL
);


ALTER TABLE deal.payment_schedule OWNER TO admin;

--
-- Name: payment_schedule_payment_schedule_id_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.payment_schedule_payment_schedule_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.payment_schedule_payment_schedule_id_seq OWNER TO admin;

--
-- Name: payment_schedule_payment_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: deal; Owner: admin
--

ALTER SEQUENCE deal.payment_schedule_payment_schedule_id_seq OWNED BY deal.payment_schedule.payment_schedule_id;


--
-- Name: status_history_pk_seq; Type: SEQUENCE; Schema: deal; Owner: admin
--

CREATE SEQUENCE deal.status_history_pk_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE deal.status_history_pk_seq OWNER TO admin;

--
-- Name: status_history; Type: TABLE; Schema: deal; Owner: admin
--

CREATE TABLE deal.status_history (
                                        status_history_id integer DEFAULT nextval('deal.status_history_pk_seq'::regclass) NOT NULL,
                                        previous_status_id integer DEFAULT 1 NOT NULL,
                                        current_status_id integer NOT NULL,
                                        updated timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                        change_type_id integer DEFAULT 2 NOT NULL,
                                        application_id integer NOT NULL
);


ALTER TABLE deal.status_history OWNER TO admin;

--
-- Name: application application_id; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.application ALTER COLUMN application_id SET DEFAULT nextval('deal.application_application_id_seq'::regclass);


--
-- Name: application_status application_status; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.application_status ALTER COLUMN application_status_id SET DEFAULT nextval('deal.application_status_application_status_seq'::regclass);


--
-- Name: change_type change_type_id; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.change_type ALTER COLUMN change_type_id SET DEFAULT nextval('deal.change_type_change_type_id_seq'::regclass);


--
-- Name: credit credit_id; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.credit ALTER COLUMN credit_id SET DEFAULT nextval('deal.credit_credit_id_seq'::regclass);


--
-- Name: employment employment_id; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.employment ALTER COLUMN employment_id SET DEFAULT nextval('deal.employment_employment_id_seq'::regclass);


--
-- Name: gender_type gender_type_id; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.gender_type ALTER COLUMN gender_type_id SET DEFAULT nextval('deal.gender_type_gender_type_id_seq'::regclass);


--
-- Name: marital_status_type marital_status_id; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.marital_status_type ALTER COLUMN marital_status_id SET DEFAULT nextval('deal.marital_status_type_marital_status_seq'::regclass);


--
-- Name: passport passport_id; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.passport ALTER COLUMN passport_id SET DEFAULT nextval('deal.passport_passport_id_seq'::regclass);


--
-- Name: payment_schedule payment_schedule_id; Type: DEFAULT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.payment_schedule ALTER COLUMN payment_schedule_id SET DEFAULT nextval('deal.payment_schedule_payment_schedule_id_seq'::regclass);


--
-- Data for Name: application; Type: TABLE DATA; Schema: deal; Owner: admin
--



--
-- Data for Name: application_status; Type: TABLE DATA; Schema: deal; Owner: admin
--



--
-- Data for Name: change_type; Type: TABLE DATA; Schema: deal; Owner: admin
--

INSERT INTO deal.change_type VALUES (1, 'AUTOMATIC');
INSERT INTO deal.change_type VALUES (2, 'MANUAL');


--
-- Data for Name: client; Type: TABLE DATA; Schema: deal; Owner: admin
--



--
-- Data for Name: credit; Type: TABLE DATA; Schema: deal; Owner: admin
--



--
-- Data for Name: credit_status; Type: TABLE DATA; Schema: deal; Owner: admin
--

INSERT INTO deal.credit_status VALUES (1, 'CALCULATED');
INSERT INTO deal.credit_status VALUES (2, 'ISSUED');


--
-- Data for Name: employment; Type: TABLE DATA; Schema: deal; Owner: admin
--



--
-- Data for Name: gender_type; Type: TABLE DATA; Schema: deal; Owner: admin
--

INSERT INTO deal.gender_type VALUES (1, 'MALE');
INSERT INTO deal.gender_type VALUES (2, 'FEMALE');
INSERT INTO deal.gender_type VALUES (3, 'NON_BINARY');
INSERT INTO deal.gender_type VALUES (4, 'TRANSGENDER');
INSERT INTO deal.gender_type VALUES (5, 'DIFFERENT');


--
-- Data for Name: log; Type: TABLE DATA; Schema: deal; Owner: admin
--

INSERT INTO deal.log VALUES (1, 'init message', '2023-10-14 20:01:01.667887');


--
-- Data for Name: marital_status_type; Type: TABLE DATA; Schema: deal; Owner: admin
--

INSERT INTO deal.marital_status_type VALUES (1, 'MARRIED');
INSERT INTO deal.marital_status_type VALUES (2, 'SINGLE');
INSERT INTO deal.marital_status_type VALUES (3, 'DIVORCED');
INSERT INTO deal.marital_status_type VALUES (4, 'WIDOWED');


INSERT INTO deal.application_status (application_status_id, status_type) VALUES (DEFAULT, 'PREAPPROVAL');
INSERT INTO deal.application_status (application_status_id, status_type) VALUES (DEFAULT, 'APPROVED');
INSERT INTO deal.application_status (application_status_id, status_type) VALUES (DEFAULT, 'CC_DENIED');
INSERT INTO deal.application_status (application_status_id, status_type) VALUES (DEFAULT, 'CC_APPROVED');
INSERT INTO deal.application_status (application_status_id, status_type) VALUES (DEFAULT, 'PREPARE_DOCUMENTS');
INSERT INTO deal.application_status (application_status_id, status_type) VALUES (DEFAULT, 'CLIENT_DENIED');
INSERT INTO deal.application_status (application_status_id, status_type) VALUES (DEFAULT, 'DOCUMENT_SIGNED');
INSERT INTO deal.application_status (application_status_id, status_type) VALUES (DEFAULT, 'CREDIT_ISSUED');

--
-- Data for Name: passport; Type: TABLE DATA; Schema: deal; Owner: admin
--



--
-- Data for Name: payment_schedule; Type: TABLE DATA; Schema: deal; Owner: admin
--



--
-- Data for Name: status_history; Type: TABLE DATA; Schema: deal; Owner: admin
--



--
-- Name: application_application_id_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.application_application_id_seq', 1, false);


--
-- Name: application_status_application_status_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.application_status_application_status_seq', 1, false);


--
-- Name: change_type_change_type_id_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.change_type_change_type_id_seq', 2, true);


--
-- Name: client_pk_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.client_pk_seq', 1, true);


--
-- Name: credit_credit_id_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.credit_credit_id_seq', 1, false);


--
-- Name: credit_status_id_fk_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.credit_status_id_fk_seq', 2, true);


--
-- Name: employment_employment_id_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.employment_employment_id_seq', 1, false);


--
-- Name: gender_type_gender_type_id_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.gender_type_gender_type_id_seq', 5, true);


--
-- Name: marital_status_type_marital_status_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.marital_status_type_marital_status_seq', 4, true);


--
-- Name: passport_passport_id_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.passport_passport_id_seq', 1, true);


--
-- Name: payment_schedule_payment_schedule_id_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.payment_schedule_payment_schedule_id_seq', 1, false);


--
-- Name: status_history_pk_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.status_history_pk_seq', 1, false);


--
-- Name: log_pk_seq; Type: SEQUENCE SET; Schema: deal; Owner: admin
--

SELECT pg_catalog.setval('deal.log_pk_seq', 2, true);


--
-- Name: application application_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.application
    ADD CONSTRAINT application_pk PRIMARY KEY (application_id);


--
-- Name: application_status application_status_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.application_status
    ADD CONSTRAINT application_status_pk PRIMARY KEY (application_status_id);


--
-- Name: change_type change_type_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.change_type
    ADD CONSTRAINT change_type_pk PRIMARY KEY (change_type_id);


--
-- Name: client client_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.client
    ADD CONSTRAINT client_pk PRIMARY KEY (client_id);


--
-- Name: credit credit_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.credit
    ADD CONSTRAINT credit_pk PRIMARY KEY (credit_id);


--
-- Name: credit_status credit_status_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.credit_status
    ADD CONSTRAINT credit_status_pk PRIMARY KEY (credit_status_id);


--
-- Name: employment employment_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.employment
    ADD CONSTRAINT employment_pk PRIMARY KEY (employment_id);


--
-- Name: gender_type gender_type_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.gender_type
    ADD CONSTRAINT gender_type_pk PRIMARY KEY (gender_type_id);


--
-- Name: marital_status_type marital_status_type_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.marital_status_type
    ADD CONSTRAINT marital_status_type_pk PRIMARY KEY (marital_status_id);


--
-- Name: passport passport_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.passport
    ADD CONSTRAINT passport_pk PRIMARY KEY (passport_id);


--
-- Name: payment_schedule payment_schedule_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.payment_schedule
    ADD CONSTRAINT payment_schedule_pk PRIMARY KEY (payment_schedule_id);


--
-- Name: status_history status_history_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.status_history
    ADD CONSTRAINT status_history_pk PRIMARY KEY (status_history_id);


--
-- Name: log log_pk; Type: CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.log
    ADD CONSTRAINT log_pk PRIMARY KEY (id);


--
-- Name: application application_client_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.application
    ADD CONSTRAINT application_client_id_fk FOREIGN KEY (client_id) REFERENCES deal.client(client_id);


--
-- Name: application application_credit_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.application
    ADD CONSTRAINT application_credit_id_fk FOREIGN KEY (credit_id) REFERENCES deal.credit(credit_id);


--
-- Name: client client_gender_type_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.client
    ADD CONSTRAINT client_gender_type_id_fk FOREIGN KEY (gender_id) REFERENCES deal.gender_type(gender_type_id);


--
-- Name: client client_marital_status_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.client
    ADD CONSTRAINT client_marital_status_id_fk FOREIGN KEY (marital_status) REFERENCES deal.marital_status_type(marital_status_id);


--
-- Name: credit credit_credit_status_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.credit
    ADD CONSTRAINT credit_credit_status_id_fk FOREIGN KEY (credit_status) REFERENCES deal.credit_status(credit_status_id);


--
-- Name: employment employment_client_client_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.employment
    ADD CONSTRAINT employment_client_client_id_fk FOREIGN KEY (client_id) REFERENCES deal.client(client_id)
    ON DELETE CASCADE;


--
-- Name: passport passport_client_client_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.passport
    ADD CONSTRAINT passport_client_client_id_fk FOREIGN KEY (client_id) REFERENCES deal.client(client_id)
    ON DELETE CASCADE;


--
-- Name: payment_schedule payment_schedule_credit_credit_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.payment_schedule
    ADD CONSTRAINT payment_schedule_credit_credit_id_fk FOREIGN KEY (credit_id) REFERENCES deal.credit(credit_id)
    ON DELETE CASCADE;


--
-- Name: status_history status_history_application_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.status_history
    ADD CONSTRAINT status_history_application_id_fk FOREIGN KEY (application_id) REFERENCES deal.application(application_id);


--
-- Name: status_history status_history_change_type_id_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.status_history
    ADD CONSTRAINT status_history_change_type_id_fk FOREIGN KEY (change_type_id) REFERENCES deal.change_type(change_type_id);


--
-- Name: status_history status_history_current_status_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.status_history
    ADD CONSTRAINT status_history_current_status_fk FOREIGN KEY (current_status_id) REFERENCES deal.application_status(application_status_id);


--
-- Name: status_history status_history_prev_status_fk; Type: FK CONSTRAINT; Schema: deal; Owner: admin
--

ALTER TABLE ONLY deal.status_history
    ADD CONSTRAINT status_history_prev_status_fk FOREIGN KEY (previous_status_id) REFERENCES deal.application_status(application_status_id);


--
-- PostgreSQL database dump complete
--

