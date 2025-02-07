--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 17.1

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
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 4369 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 227 (class 1259 OID 16855)
-- Name: age_gender_statistics; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.age_gender_statistics (
                                              age_gender_statistics_id integer NOT NULL,
                                              gender character varying(10) NOT NULL,
                                              age integer NOT NULL,
                                              avg_height double precision NOT NULL,
                                              avg_weight double precision NOT NULL
);


--
-- TOC entry 226 (class 1259 OID 16854)
-- Name: age_gender_statistics_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.age_gender_statistics_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4370 (class 0 OID 0)
-- Dependencies: 226
-- Name: age_gender_statistics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.age_gender_statistics_id_seq OWNED BY public.age_gender_statistics.age_gender_statistics_id;


--
-- TOC entry 218 (class 1259 OID 16687)
-- Name: children; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.children (
                                 child_id integer NOT NULL,
                                 user_id integer NOT NULL,
                                 name character varying(50) NOT NULL,
                                 birth_date date,
                                 gender character varying(10),
                                 blood_type character varying(10),
                                 created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 updated_at timestamp without time zone,
                                 birth_weight numeric(4,1),
                                 birth_time time without time zone,
                                 profile_picture character varying(500),
                                 birth_height numeric(4,1)
);


--
-- TOC entry 217 (class 1259 OID 16686)
-- Name: children_child_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.children_child_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4371 (class 0 OID 0)
-- Dependencies: 217
-- Name: children_child_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.children_child_id_seq OWNED BY public.children.child_id;


--
-- TOC entry 222 (class 1259 OID 16715)
-- Name: photo; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.photo (
                              photo_id integer NOT NULL,
                              record_id integer NOT NULL,
                              url character varying(255),
                              created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 16714)
-- Name: photo_photo_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.photo_photo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4372 (class 0 OID 0)
-- Dependencies: 221
-- Name: photo_photo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.photo_photo_id_seq OWNED BY public.photo.photo_id;


--
-- TOC entry 220 (class 1259 OID 16700)
-- Name: record; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.record (
                               record_id integer NOT NULL,
                               child_id integer NOT NULL,
                               record_date date NOT NULL,
                               height numeric(4,1),
                               weight numeric(4,1),
                               content text,
                               video_result integer,
                               created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                               updated_at timestamp without time zone,
                               title text
);


--
-- TOC entry 219 (class 1259 OID 16699)
-- Name: record_record_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.record_record_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4373 (class 0 OID 0)
-- Dependencies: 219
-- Name: record_record_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.record_record_id_seq OWNED BY public.record.record_id;


--
-- TOC entry 225 (class 1259 OID 16815)
-- Name: refresh_tokens; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.refresh_tokens (
                                       user_id integer NOT NULL,
                                       token text NOT NULL,
                                       expiry_date timestamp without time zone NOT NULL
);


--
-- TOC entry 216 (class 1259 OID 16661)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
                              user_id integer NOT NULL,
                              kakao_id bigint NOT NULL,
                              created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              updated_at timestamp without time zone,
                              email character varying(255)
);


--
-- TOC entry 215 (class 1259 OID 16660)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4374 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 224 (class 1259 OID 16728)
-- Name: weekly_report; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.weekly_report (
                                      report_id integer NOT NULL,
                                      child_id integer NOT NULL,
                                      risk_level integer,
                                      feedback text,
                                      created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                      is_read boolean NOT NULL,
                                      video_result_count numeric(4,4)
);


--
-- TOC entry 223 (class 1259 OID 16727)
-- Name: weekly_report_report_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.weekly_report_report_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4375 (class 0 OID 0)
-- Dependencies: 223
-- Name: weekly_report_report_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.weekly_report_report_id_seq OWNED BY public.weekly_report.report_id;


--
-- TOC entry 4179 (class 2604 OID 16877)
-- Name: age_gender_statistics age_gender_statistics_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.age_gender_statistics ALTER COLUMN age_gender_statistics_id SET DEFAULT nextval('public.age_gender_statistics_id_seq'::regclass);


--
-- TOC entry 4171 (class 2604 OID 16690)
-- Name: children child_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.children ALTER COLUMN child_id SET DEFAULT nextval('public.children_child_id_seq'::regclass);


--
-- TOC entry 4175 (class 2604 OID 16718)
-- Name: photo photo_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.photo ALTER COLUMN photo_id SET DEFAULT nextval('public.photo_photo_id_seq'::regclass);


--
-- TOC entry 4173 (class 2604 OID 16703)
-- Name: record record_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.record ALTER COLUMN record_id SET DEFAULT nextval('public.record_record_id_seq'::regclass);


--
-- TOC entry 4169 (class 2604 OID 16781)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 4177 (class 2604 OID 16731)
-- Name: weekly_report report_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.weekly_report ALTER COLUMN report_id SET DEFAULT nextval('public.weekly_report_report_id_seq'::regclass);


--
-- TOC entry 4362 (class 0 OID 16855)
-- Dependencies: 227
-- Data for Name: age_gender_statistics; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (295, 'M', 0, 50.8, 3.4);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (296, 'W', 0, 50.1, 3.3);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (297, 'M', 1, 55.3, 4.56);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (299, 'W', 1, 54.2, 4.36);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (301, 'M', 2, 59, 5.82);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (303, 'W', 2, 58, 5.49);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (305, 'M', 3, 62.5, 6.81);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (307, 'W', 3, 61.1, 6.32);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (309, 'M', 4, 65.2, 7.56);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (311, 'W', 4, 63.8, 7.09);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (313, 'M', 5, 66.8, 7.93);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (315, 'W', 5, 65.7, 7.51);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (317, 'M', 6, 69, 8.52);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (319, 'W', 6, 69.1, 8.25);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (321, 'M', 7, 70.4, 8.74);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (323, 'W', 7, 69.1, 8.25);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (325, 'M', 8, 71.9, 9.03);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (327, 'W', 8, 70.5, 8.48);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (329, 'M', 9, 73.5, 9.42);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (331, 'W', 9, 72.2, 8.85);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (333, 'M', 10, 74.6, 9.68);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (335, 'W', 10, 73.5, 9.24);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (337, 'M', 11, 76.5, 9.77);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (339, 'W', 11, 75.6, 9.28);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (341, 'M', 12, 77.8, 10.42);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (342, 'M', 13, 77.8, 10.42);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (343, 'M', 14, 77.8, 10.42);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (345, 'W', 12, 76.9, 10.01);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (346, 'W', 13, 76.9, 10.01);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (347, 'W', 14, 76.9, 10.01);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (349, 'M', 15, 80.1, 11);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (350, 'M', 16, 80.1, 11);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (351, 'M', 17, 80.1, 11);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (353, 'W', 15, 79.2, 10.52);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (354, 'W', 16, 79.2, 10.52);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (355, 'W', 17, 79.2, 10.52);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (357, 'M', 18, 82.6, 11.72);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (358, 'M', 19, 82.6, 11.72);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (359, 'M', 20, 82.6, 11.72);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (361, 'W', 18, 81.8, 11.23);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (362, 'W', 19, 81.8, 11.23);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (363, 'W', 20, 81.8, 11.23);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (365, 'M', 21, 85.1, 12.3);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (366, 'M', 22, 85.1, 12.3);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (367, 'M', 23, 85.1, 12.3);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (369, 'W', 21, 84.4, 12.03);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (370, 'W', 22, 84.4, 12.03);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (371, 'W', 23, 84.4, 12.03);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (373, 'M', 24, 87.7, 12.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (374, 'M', 25, 87.7, 12.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (375, 'M', 26, 87.7, 12.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (376, 'M', 27, 87.7, 12.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (377, 'M', 28, 87.7, 12.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (378, 'M', 29, 87.7, 12.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (380, 'W', 24, 87, 12.51);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (381, 'W', 25, 87, 12.51);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (382, 'W', 26, 87, 12.51);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (383, 'W', 27, 87, 12.51);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (384, 'W', 28, 87, 12.51);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (385, 'W', 29, 87, 12.51);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (387, 'M', 30, 92.2, 14.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (388, 'M', 31, 92.2, 14.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (389, 'M', 32, 92.2, 14.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (390, 'M', 33, 92.2, 14.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (391, 'M', 34, 92.2, 14.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (392, 'M', 35, 92.2, 14.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (394, 'W', 30, 90.9, 13.35);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (395, 'W', 31, 90.9, 13.35);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (396, 'W', 32, 90.9, 13.35);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (397, 'W', 33, 90.9, 13.35);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (398, 'W', 34, 90.9, 13.35);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (399, 'W', 35, 90.9, 13.35);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (401, 'M', 36, 95.7, 15.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (402, 'M', 37, 95.7, 15.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (403, 'M', 38, 95.7, 15.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (404, 'M', 39, 95.7, 15.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (405, 'M', 40, 95.7, 15.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (406, 'M', 41, 95.7, 15.08);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (408, 'W', 36, 94.2, 14.16);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (409, 'W', 37, 94.2, 14.16);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (410, 'W', 38, 94.2, 14.16);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (411, 'W', 39, 94.2, 14.16);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (412, 'W', 40, 94.2, 14.16);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (413, 'W', 41, 94.2, 14.16);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (415, 'M', 42, 99.8, 15.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (416, 'M', 43, 99.8, 15.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (417, 'M', 44, 99.8, 15.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (418, 'M', 45, 99.8, 15.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (419, 'M', 46, 99.8, 15.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (431, 'M', 50, 103.5, 16.99);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (432, 'M', 51, 103.5, 16.99);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (433, 'M', 52, 103.5, 16.99);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (434, 'M', 53, 103.5, 16.99);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (436, 'W', 48, 102.1, 16.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (437, 'W', 49, 102.1, 16.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (438, 'W', 50, 102.1, 16.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (439, 'W', 51, 102.1, 16.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (440, 'W', 52, 102.1, 16.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (420, 'M', 47, 99.8, 15.94);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (422, 'W', 42, 98.7, 15.37);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (423, 'W', 43, 98.7, 15.37);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (424, 'W', 44, 98.7, 15.37);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (425, 'W', 45, 98.7, 15.37);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (426, 'W', 46, 98.7, 15.37);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (427, 'W', 47, 98.7, 15.37);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (429, 'M', 48, 103.5, 16.99);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (430, 'M', 49, 103.5, 16.99);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (441, 'W', 53, 102.1, 16.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (443, 'M', 54, 106.6, 17.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (444, 'M', 55, 106.6, 17.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (445, 'M', 56, 106.6, 17.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (446, 'M', 57, 106.6, 17.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (447, 'M', 58, 106.6, 17.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (448, 'M', 59, 106.6, 17.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (450, 'W', 54, 105.4, 17.31);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (451, 'W', 55, 105.4, 17.31);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (452, 'W', 56, 105.4, 17.31);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (453, 'W', 57, 105.4, 17.31);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (454, 'W', 58, 105.4, 17.31);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (455, 'W', 59, 105.4, 17.31);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (457, 'M', 60, 109.6, 18.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (458, 'M', 61, 109.6, 18.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (459, 'M', 62, 109.6, 18.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (460, 'M', 63, 109.6, 18.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (461, 'M', 64, 109.6, 18.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (462, 'M', 65, 109.6, 18.98);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (464, 'W', 60, 108.6, 18.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (465, 'W', 61, 108.6, 18.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (466, 'W', 62, 108.6, 18.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (467, 'W', 63, 108.6, 18.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (468, 'W', 64, 108.6, 18.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (469, 'W', 65, 108.6, 18.43);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (471, 'M', 66, 112.9, 20.15);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (472, 'M', 67, 112.9, 20.15);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (473, 'M', 68, 112.9, 20.15);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (474, 'M', 69, 112.9, 20.15);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (475, 'M', 70, 112.9, 20.15);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (476, 'M', 71, 112.9, 20.15);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (478, 'W', 66, 112.1, 19.74);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (479, 'W', 67, 112.1, 19.74);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (480, 'W', 68, 112.1, 19.74);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (481, 'W', 69, 112.1, 19.74);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (482, 'W', 70, 112.1, 19.74);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (483, 'W', 71, 112.1, 19.74);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (485, 'M', 72, 115.8, 21.41);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (486, 'M', 73, 115.8, 21.41);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (487, 'M', 74, 115.8, 21.41);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (488, 'M', 75, 115.8, 21.41);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (489, 'M', 76, 115.8, 21.41);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (490, 'M', 77, 115.8, 21.41);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (492, 'W', 72, 114.7, 20.68);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (493, 'W', 73, 114.7, 20.68);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (494, 'W', 74, 114.7, 20.68);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (495, 'W', 75, 114.7, 20.68);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (496, 'W', 76, 114.7, 20.68);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (497, 'W', 77, 114.7, 20.68);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (499, 'M', 78, 118.5, 22.57);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (500, 'M', 79, 118.5, 22.57);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (501, 'M', 80, 118.5, 22.57);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (502, 'M', 81, 118.5, 22.57);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (503, 'M', 82, 118.5, 22.57);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (504, 'M', 83, 118.5, 22.57);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (505, 'M', 84, 118.5, 22.57);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (506, 'W', 78, 117.5, 21.96);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (507, 'W', 79, 117.5, 21.96);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (508, 'W', 80, 117.5, 21.96);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (509, 'W', 81, 117.5, 21.96);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (510, 'W', 82, 117.5, 21.96);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (511, 'W', 83, 117.5, 21.96);
INSERT INTO public.age_gender_statistics (age_gender_statistics_id, gender, age, avg_height, avg_weight) VALUES (512, 'W', 84, 117.5, 21.96);


--
-- TOC entry 4353 (class 0 OID 16687)
-- Dependencies: 218
-- Data for Name: children; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (108, 21, '한정우', '2023-10-15', 'M', 'A', '2024-11-19 17:04:37.442481', NULL, 2.5, '13:15:00', '4fc943fd-4683-4767-9b40-997c74938eca_KakaoTalk_20240707_222236597.jpg', 16.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (102, 27, '프롬이', '2023-11-01', 'M', 'AB', '2024-11-20 12:17:58.907346', NULL, 3.4, '05:12:00', NULL, 55.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (110, 33, '박인욱', '2024-11-25', 'M', 'A', '2024-11-25 07:20:44.510955', NULL, 3.5, '04:19:00', NULL, 45.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (109, 32, '프로미스', '2023-06-20', 'W', 'B', '2024-11-20 13:11:32.811424', NULL, 3.3, '11:23:00', NULL, 50.1);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (86, 23, 'shtest', '2024-11-01', 'M', 'A', '2024-11-14 21:26:42.639606', NULL, 1.0, '01:01:00', '871e781b-e8ef-4a97-ac0b-5370a6616621_test.png', 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (96, 23, 'te', '2024-01-01', 'M', 'B', '2024-11-15 09:45:23.901443', NULL, 1.0, '13:01:00', '69c666c3-d99c-4cd3-9f91-7547e3680a9d_test.png', 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (97, 19, '바게트빵', '2024-11-01', 'M', 'A', '2024-11-15 09:53:21.037141', NULL, 3.0, '01:01:00', 'e33010d3-2388-4f35-ac91-222c6f1463fb_1000007461.jpg', 50.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (98, 21, '귀요미', '2024-09-10', 'M', 'A', '2024-11-15 10:08:45.130821', NULL, 2.0, '14:13:00', NULL, 10.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (87, 19, '사진수정', '2024-11-01', 'M', 'A', '2024-11-18 12:50:39.112188', NULL, 1.0, '01:01:00', 'd1ce2ad5-44aa-43f8-81ae-d49cab6e753c_1000007462.jpg', 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (106, 19, '아이추가테스트', '2024-11-18', 'M', 'A', '2024-11-18 12:51:21.342567', NULL, 1.0, '01:01:00', NULL, 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (107, 19, '프로필', '2024-11-18', 'M', 'A', '2024-11-18 12:52:32.755417', NULL, 1.0, '01:01:00', 'e8a19126-7e6f-4a7a-8461-8eb6df22fdc9_1000001181.png', 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (72, 19, '윤채원', '2024-11-14', 'M', 'AB', '2024-11-14 12:29:43.86819', NULL, 1.1, '01:01:00', '8a36a5de-2642-4303-b8ae-47df85c464a8_스크린샷 2024-11-04 154252.png', 1.1);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (79, 19, '아아', '2024-11-11', 'M', 'A', '2024-11-14 20:36:26.920225', NULL, 11.5, '11:11:00', NULL, 11.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (81, 19, 'shanTest', '2024-11-01', 'M', 'A', '2024-11-14 21:14:41.745914', NULL, 1.0, '01:01:00', NULL, 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (82, 19, 'shanTestTwo', '2024-11-01', 'M', 'A', '2024-11-14 21:15:05.731454', NULL, 1.0, '13:01:00', '081cc8b6-34d8-4a27-a9ff-0934c89e5af5_스크린샷 2024-10-21 205510.png', 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (84, 19, 'shanTestThree', '2024-11-01', 'M', 'B', '2024-11-14 21:21:00.081161', NULL, 1.0, '01:01:00', '1d09e598-a2d9-4140-979d-8e89f639471b_스크린샷 2024-10-16 114431.png', 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (85, 19, 'shan', '2024-01-01', 'M', 'A', '2024-11-14 21:23:51.73547', NULL, 1.0, '13:01:00', 'f652f375-7e4f-42bf-bb61-5b2b6703c755_test.png', 1.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (83, 32, '한정우', '2023-10-13', 'M', 'A', '2024-11-14 21:15:14.086523', NULL, 2.0, '08:13:00', NULL, 12.0);
INSERT INTO public.children (child_id, user_id, name, birth_date, gender, blood_type, created_at, updated_at, birth_weight, birth_time, profile_picture, birth_height) VALUES (105, 32, '도담이', '2024-11-11', 'M', 'A', '2024-11-15 11:19:29.467653', NULL, 3.0, '11:11:00', NULL, 49.0);


--
-- TOC entry 4357 (class 0 OID 16715)
-- Dependencies: 222
-- Data for Name: photo; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (239, 263, 'a0353fe5-ab80-4088-a581-1cedacc8f4d6_스크린샷 2024-10-11 183130.png', '2024-11-15 09:46:08.419832');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (246, 269, '1c604879-f110-438c-a503-f809b6c310de_fqwess.PNG', '2024-11-19 14:06:24.110341');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (253, 305, '9b598de8-897c-42ee-bec8-d78d61f01a30_1000007885.jpg', '2024-11-21 04:27:20.314811');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (254, 305, '46942f89-36c8-477c-b056-c80cd51861ae_1000007886.jpg', '2024-11-21 04:27:20.325293');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (218, 238, '2b4fd782-7cc4-47d6-8725-8aa2a7a7e695_7s4GXa6qEr63LOAty6OVncbYBQ_iatMweet-dqVDr5xrBDlbxq86axzF4JynIxviMwLgBTU0m1P1IAQqew4DOSTJAUm-xfAbZGZVne66HEcVHh021lQbdb7FkcNmc-y79kLH97opk0czMYZUN_mjww.webp', '2024-11-14 21:16:05.137551');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (240, 264, '489ba54c-968d-4542-89ea-d1b95c0728c7_1000007459.jpg', '2024-11-15 09:53:45.96347');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (255, 308, '0c156b06-8ba7-4053-b31c-2116643cb3f9_IMG_8440.jpeg', '2025-01-07 20:14:29.917702');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (219, 244, '53518083-7163-4a50-9930-d01631544846_1000007462.jpg', '2024-11-14 21:35:43.280551');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (248, 272, '3edbc492-5047-4f22-a629-82d1090df4c7_1.jpg', '2024-11-20 12:29:27.492432');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (249, 273, 'd2d168be-74ab-4ff8-9b22-267b837df836_2.jpg', '2024-11-20 12:31:01.78839');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (250, 274, '6c319138-3343-4c7d-9663-3fa060e63ec4_3.jpg', '2024-11-20 12:32:22.981738');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (251, 279, '6844723b-4162-449c-b414-c1f8de4b209c_4.jpg', '2024-11-20 12:35:21.770918');
INSERT INTO public.photo (photo_id, record_id, url, created_at) VALUES (252, 282, '84595618-73da-497d-8267-68cf6b62ab89_5.jpg', '2024-11-20 12:36:25.840123');


--
-- TOC entry 4355 (class 0 OID 16700)
-- Dependencies: 220
-- Data for Name: record; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (298, 109, '2024-11-11', NULL, NULL, '오늘 아기가 "아~" 하고 소리를 내면서 마치 대화하는 것처럼 보였어요. 엄마와 눈을 마주치면서 소리 내고, 손을 흔들고. 그 모습이 정말 사랑스럽고 신기했어요. 아기가 점점 의사소통을 시도하는 것 같아서 뿌듯했어요. 이제 아기와 더 많은 대화를 나누고 싶은 마음이에요. 언젠가는 아기와 대화할 수 있을 거라고 생각하니까 정말 기대돼요.', NULL, '2024-11-20 13:17:52.432348', NULL, '아기의 작은 대화');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (305, 109, '2024-11-18', 51.0, 5.2, '오늘 화창한 날씨에 아이와 산책을 나갔다', NULL, '2024-11-21 04:27:19.410745', NULL, '아이와 산책');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (238, 83, '2024-11-14', 20.0, 1.0, '핑', NULL, '2024-11-14 21:16:04.739363', NULL, '츄핑츄핑');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (239, 83, '2024-11-13', 10.0, 1.0, '빠리뽕ㄴㄴㅇ', NULL, '2024-11-14 21:18:06.238957', NULL, '삐리');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (240, 83, '2024-11-12', 20.0, 1.0, '빠빵', NULL, '2024-11-14 21:18:29.906079', NULL, '빼리뽀리빵');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (241, 84, '2024-11-11', NULL, NULL, 'rr', NULL, '2024-11-14 21:22:30.888209', NULL, 'rr');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (242, 84, '2024-11-12', NULL, NULL, 'dd', NULL, '2024-11-14 21:22:41.133806', NULL, 'dd');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (243, 84, '2024-11-13', NULL, NULL, 'dd', NULL, '2024-11-14 21:22:44.591741', NULL, 'dd');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (244, 87, '2024-11-14', NULL, NULL, '테스트1', NULL, '2024-11-14 21:35:42.945339', NULL, '테스트');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (245, 86, '2024-11-13', NULL, NULL, '1', NULL, '2024-11-14 21:37:03.163317', NULL, '1');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (246, 86, '2024-11-11', NULL, NULL, '1', NULL, '2024-11-14 21:37:07.998658', NULL, '1');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (247, 86, '2024-11-12', NULL, NULL, '1', NULL, '2024-11-14 21:37:11.5182', NULL, '1');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (274, 102, '2024-11-03', 78.0, 10.5, '오늘 아기가 물건을 잡으려 했어요. 그런데 손끝만 집중해서 그걸 쥐려는 모습이었어요. 다른 아기들처럼 손을 크게 뻗고 자연스럽게 잡는 모습이 아니라서 조금 신경이 쓰였어요. 아기 손을 잡아주면 좋아하는 모습은 여전히 귀엽지만, 자주 손끝만 잡으려는 게 좀 불편해 보였어요. 아기가 점점 더 똑똑해지길 바라는데, 이런 모습이 조금 걱정돼요.', NULL, '2024-11-20 12:32:22.834397', NULL, '손끝에만 집중');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (248, 86, '2024-11-14', NULL, NULL, '1', NULL, '2024-11-14 21:37:20.045806', NULL, '1');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (281, 102, '2024-11-10', NULL, NULL, '오늘 아기가 계속해서 앉아만 있거나 손에 아무것도 쥐지 않으려고 하더라고요. 보통 아기들은 앉아 있을 때 손으로 물건을 잡거나 주변을 탐색하는데, 우리 아기는 그런 행동을 거의 하지 않았어요. 다른 아기들처럼 자연스럽게 물건을 만지고, 놀고 싶어 할 거라고 기대했는데, 오늘은 아기가 가만히 있길 원했어요. 조금 불안한 마음이 드는 하루였네요.', NULL, '2024-11-20 12:35:52.801325', NULL, '가만히 앉아만 있어요');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (273, 102, '2024-11-02', 77.9, 10.5, '오늘 아기는 좀처럼 웃지 않았어요. 가끔 웃음을 보일 때도 있지만, 평소처럼 반응을 많이 보이지 않아서 조금은 속상했어요. 다른 아기들은 엄마 아빠가 장난을 쳐주면 바로 반응하는데, 우리 아기는 그냥 가만히 보고만 있더라고요. 아기가 웃을 때마다 너무 기뻤던 지난 날들이 떠오르네요. 요즘은 웃는 게 좀 줄어든 것 같아서 마음이 무겁네요.', NULL, '2024-11-20 12:31:01.678463', NULL, '웃음이 적었던 하루');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (278, 102, '2024-11-07', 78.2, 10.7, '오늘 아기에게 여러 가지 소리를 들려줬어요. 엄마가 소리도 내보고, 장난감을 흔들면서 소리를 냈지만 아기가 특별한 반응을 보이지 않았어요. 보통 아기들은 소리에 민감하게 반응하는데, 우리 아기는 소리를 듣고도 별다른 반응을 보이지 않아서 좀 의아했어요. 다른 아기들과 차이가 있는 것 같아서, 아기에게 무엇이 잘못된 건 아닌지 걱정이 됩니다.', NULL, '2024-11-20 12:34:24.670333', NULL, '소리에 대한 반응');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (275, 102, '2024-11-04', 78.0, 10.5, '오늘 아기가 처음으로 손뼉을 쳤어요! 엄마가 손뼉을 치면서 "잘했어요!"라고 하자 아기도 손을 흔들며 반응했어요. 너무 귀엽고 뿌듯한 순간이었죠. 하지만 아기가 손뼉을 칠 때, 눈을 맞추지는 않고 무표정한 얼굴로 하더라고요. 그 모습이 조금 이상했지만, 그래도 큰 진전이니까 기쁘게 생각하려고 해요.', NULL, '2024-11-20 12:33:28.49939', NULL, '처음으로 손뼉을 쳐보았어요');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (272, 102, '2024-11-01', 77.8, 10.4, '오늘로 아기가 태어난 지 1년이 됐다. 정말 시간이 너무 빨리 지나간 것 같아. 아기와 함께 보낸 첫 번째 생일, 너무 행복하고 감격스러웠다. 생일 케이크도 준비하고, 작은 축하 파티도 했는데 아기는 그저 내내 조용히 있었다. 모두가 즐거운 분위기였지만, 아기가 너무 잠만 자서 조금 걱정이 되기도 했어요. 이제는 좀 더 활발해지길 바라는 마음이 커져요.', NULL, '2024-11-20 12:29:27.343685', NULL, '아기, 1살 생일을 맞다');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (263, 96, '2024-11-15', NULL, NULL, '1', NULL, '2024-11-15 09:46:08.10804', NULL, '1');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (264, 97, '2024-11-15', NULL, NULL, '사진테스트입니다.', NULL, '2024-11-15 09:53:45.766221', NULL, '사진테스트');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (267, 97, '2024-11-14', NULL, NULL, '1', NULL, '2024-11-15 12:13:38.817945', NULL, '1');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (268, 83, '2024-11-18', NULL, NULL, '테스트', NULL, '2024-11-18 12:39:20.436178', NULL, 'EC2 테스트');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (269, 98, '2024-11-19', 60.0, 10.0, '진짜 빡세요', NULL, '2024-11-19 14:06:23.511933', NULL, '배포 빡세요 빡세');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (282, 102, '2024-11-11', NULL, NULL, '아기가 요즘 자주 울지 않아요. 다른 아기들은 배고프거나 피곤하면 우는데, 우리 아기는 그런 반응이 적어요. 아기가 울지 않으면 다행이라고 생각할 수 있지만, 가끔은 아기가 너무 조용해서 뭔가 다른 문제가 있는 건 아닌지 걱정되기도 해요. 오늘도 아기가 울지 않고, 그냥 조용히 있었다는 게 마음에 걸렸어요.', NULL, '2024-11-20 12:36:25.75736', NULL, '자주 울지 않아요');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (283, 102, '2024-11-12', NULL, NULL, '오늘 아기가 좋아할 만한 장난감을 주었는데, 아기가 전혀 반응하지 않았어요. 보통 아기들은 장난감을 보면 손을 뻗어서 잡거나, 소리나 빛에 반응하는데, 우리 아기는 그저 멀뚱히 보고만 있었어요. 아기가 장난감에 관심을 보이지 않으니 조금 걱정이 되었어요. 다르게 생각하면 아기가 단순히 그 장난감을 별로 좋아하지 않는 것일 수도 있지만, 다른 이유가 있는 건 아닐까 싶은 마음에 불안한 하루였어요.', NULL, '2024-11-20 12:36:37.537464', NULL, '반응이 없는 장난감');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (284, 102, '2024-11-13', NULL, NULL, '아기가 드디어 스스로 조금씩 걷기 시작했어요! 조금씩 앞에 있는 것을 보고 발을 내딛는데, 너무 기뻐요. 그런데 걷는 모습이 조금 어색하고, 균형을 잡는 데 어려움을 겪고 있어요. 아기가 다른 아기들보다 발달이 더디게 느껴져서 마음이 아프기도 했지만, 이렇게 한 걸음씩 나아가고 있다는 게 너무 소중하고 감사해요.', NULL, '2024-11-20 12:36:52.350336', NULL, '작은 발걸음');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (285, 102, '2024-11-14', NULL, NULL, '아기가 자주 반응하지 않거나, 무언가에 관심을 보이지 않아서 요즘 조금 불안한 마음이 커져요. 주변의 다른 아기들은 새로운 것을 배우고, 더 많은 반응을 보이는데, 우리 아기는 아직 그런 모습이 덜 보여서 걱정이에요. 그래도 아기의 작은 손을 잡고 있으면 그게 가장 중요한 순간이라는 걸 깨닫고 있어요. 매일 조금씩 변해가는 아기를 지켜보며, 더 많은 사랑을 주려고 합니다.', NULL, '2024-11-20 12:37:23.477346', NULL, '발달을 기다리며');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (286, 102, '2024-11-15', NULL, NULL, '오늘은 아기가 소리에 반응하는지 확인해보려고 여러 번 장난감을 흔들어 봤다. 보통 아기들은 이런 소리에 금방 반응하는데, 우리 아기는 그 소리에 크게 반응하지 않아서 조금 걱정이 되었다. 얼굴을 가까이 대고 소리도 여러 번 냈지만, 아기의 표정은 여전히 담담해서 가슴이 좀 아팠다. 다만, 손끝으로 장난감을 살짝 만져보는 모습은 귀여웠다. 이런 부분들이 조금 걱정이 되긴 하지만, 아기의 모든 순간을 소중히 여기며 지켜보려 한다.', NULL, '2024-11-20 12:37:46.634431', NULL, '소리에도 반응이 적어');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (287, 102, '2024-11-16', NULL, NULL, '오늘 아기가 손을 움직이려고 하는데, 손끝이 잘 움직이지 않거나 원하는 대로 잘 안 되어서 조금 걱정이 됐다. 손을 움켜쥐거나 물건을 잡으려 할 때 다른 아기들처럼 잘하지 못하는 것 같아서 그 부분이 조금 신경 쓰였어요. 엄마가 손을 대면 아기는 그때서야 반응하지만, 스스로는 별다른 시도를 하지 않는 모습에 발달에 대한 고민이 살짝 커졌습니다. 그래도 아기가 나를 볼 때 가끔 웃어주는 모습에 마음이 조금 더 놓이네요.', NULL, '2024-11-20 12:38:00.685347', NULL, '손놀림이 조금 서툴러');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (280, 102, '2024-11-09', 78.3, 10.8, '오늘 아기가 처음으로 "엄마"라고 불렀어요! 정말 감동적이고, 그 한 마디에 가슴이 벅차올랐어요. 엄마가 말 걸었을 때 처음으로 반응을 보였다는 게 너무 기뻤어요. 그런데 아기가 또 미소를 지을 때는 얼굴을 딱히 바라보지 않고, 멀리 보며 웃는 것 같아서 조금 아쉬웠어요. 그래도 이렇게 말 한마디를 듣게 돼서 너무 감사하고, 아기의 반응을 기다리게 되네요.', NULL, '2024-11-20 12:35:38.41338', NULL, '처음으로 "엄마"라고 불렀어요');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (279, 102, '2024-11-08', 78.2, 10.6, '오늘 아기가 가만히 앉아 있다가 일어나려고 했지만, 혼자서는 일어나지 못했어요. 다른 아기들은 혼자서 일어나는 걸 쉽게 하는데, 우리 아기는 항상 도움을 받아야만 일어날 수 있어요. 가끔씩 다른 아기들과 비교하면 더디게 자라는 것 같아 마음이 답답하고 걱정돼요. 그래도 아기가 점점 더 잘할 수 있도록 도와줘야겠죠.

', NULL, '2024-11-20 12:35:21.693746', NULL, '혼자서 일어나지 않아요');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (276, 102, '2024-11-05', 78.1, 10.6, '오늘은 아기와 눈을 맞추려 했지만, 아기가 나를 쳐다보려 하지 않았어요. 처음엔 나랑 눈을 마주친 듯 했지만, 금세 시선을 돌리더라고요. 다른 아기들은 자연스럽게 엄마와 눈을 마주치는데, 우리 아기는 그런 모습을 잘 보이지 않아요. 아기가 다른 사람과 눈을 마주치는 걸 좀 더 기다려봐야겠지만, 가끔 불안한 마음이 드는 것도 사실이에요.', NULL, '2024-11-20 12:33:40.90914', NULL, '눈을 맞추지 않는 아기');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (277, 102, '2024-11-06', 78.1, 10.6, '오늘 아기에게 처음으로 고구마를 주었어요. 처음엔 조금 의아해하며 먹었지만, 곧 맛있다고 느끼고 손을 내밀었어요. 아기가 새로운 음식을 잘 먹는 모습을 보고 기뻤는데, 고구마를 먹고 나서 바로 잠에 들어버려서 조금 놀랐어요. 평소엔 음식을 먹고 나서 활발해지는 편인데, 오늘은 그대로 잠들어 버려서 아기가 너무 피곤한 건 아닌지 걱정됐어요.', NULL, '2024-11-20 12:33:53.059348', NULL, '새로운 음식 도전');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (288, 109, '2024-11-01', NULL, NULL, '오늘은 아기가 태어난 지 딱 1년 되는 날이에요. 아기가 태어난 날이 엊그제 같은데, 벌써 1살이라니 믿기지가 않아요. 작은 손을 흔들며 웃을 때마다 세상이 다 아름다워 보이고, 아기의 웃음소리에 하루하루 행복해집니다. 오늘은 생일 파티도 했고, 아기가 케이크 촛불을 불어보려고 했어요. 비록 불지 못했지만, 그런 순간이 너무 소중하고 감사하게 느껴져요.', NULL, '2024-11-20 13:13:26.975368', NULL, '첫 번째 생일을 맞은 아기');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (289, 109, '2024-11-02', NULL, NULL, '아기가 첫 돌을 맞이해서 사진도 많이 찍고, 가족들과 함께 좋은 시간을 보냈어요. 오늘은 아기가 처음으로 "엄마!"라고 불러줘서 정말 감동했어요. 이제는 말도 조금씩 배우고, 엄마랑 아빠를 잘 구분하는 것 같아요. 계속해서 새로운 걸 배워가는 아기를 보며 기쁨을 느껴요. 아기가 웃을 때면 세상 모든 고민이 다 사라지는 기분이에요.', NULL, '2024-11-20 13:13:52.532341', NULL, '첫 돌을 맞은 기념일');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (290, 109, '2024-11-03', NULL, NULL, '오늘은 아기에게 새로운 음식을 시도했어요. 고구마를 처음 먹어봤는데, 처음엔 조금 어색해했지만 금세 맛있어하고 손으로 고구마를 잡고 먹으려는 모습이 너무 귀여웠어요. 아기가 새로운 음식을 잘 먹고 나서는 활발하게 기어다니기 시작했어요. 아기가 점점 더 다양한 것들을 시도하는 걸 보면, 그 어떤 것보다 자랑스러워요.', NULL, '2024-11-20 13:14:03.451056', NULL, '새로운 음식 도전');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (291, 109, '2024-11-04', NULL, NULL, '아기가 오늘 처음으로 손뼉을 쳤어요! 엄마가 손뼉을 치며 칭찬해주니 아기도 덩달아 손을 흔들었어요. 이 작은 변화가 너무 기뻐서 계속해서 손뼉을 쳐보았어요. 아기가 이렇게 자주 웃고 반응하는 걸 보면, 매일매일 얼마나 고마운지 몰라요. 아기가 나중에 더 많은 걸 배울 때까지 지켜볼 생각에 설레요.', NULL, '2024-11-20 13:14:20.847335', NULL, '첫 번째 손뼉');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (292, 109, '2024-11-05', NULL, NULL, '오늘 아기가 나가려고 할 때, 손을 흔들며 "안녕!"이라고 하는 것처럼 보였어요. 정말 깜짝 놀랐어요! 아기가 이렇게 스스로 인사를 할 줄 알았다니. 어제는 손뼉을 치고, 오늘은 인사까지. 아기가 점점 더 자주 반응을 보이고, 의사소통을 시도하는 걸 보니 너무 기쁩니다. 하루하루 성장하는 모습이 너무 자랑스러워요.', NULL, '2024-11-20 13:14:30.75533', NULL, '손을 흔들며 인사');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (294, 109, '2024-11-07', NULL, NULL, '오늘 아기에게 새로운 장난감을 줬어요. 아기가 처음엔 그냥 쳐다보더니, 금세 손을 뻗어서 장난감을 쥐고 흔들었어요. 그 모습을 보고 너무 기뻤어요. 아기가 새로운 것을 배우고, 놀이에 몰두하는 모습을 보면 너무 귀엽고 뿌듯해요. 이제는 스스로 물건을 잡고 놀이도 할 수 있을 만큼 성장한 것 같아 자랑스러워요.', NULL, '2024-11-20 13:14:55.490346', NULL, '새로운 장난감');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (296, 109, '2024-11-09', NULL, NULL, '오늘 아기가 장난감을 손끝으로 만져보더니, 점점 손을 뻗어 물건을 잡으려고 했어요. 조금씩 손끝을 이용해서 잡는 모습을 보고, 아기가 점점 더 많은 걸 배우고 있구나 하는 생각에 마음이 뿌듯했어요. 아기가 주변 환경에 관심을 가지고 탐색하는 모습을 보면, 그만큼 자라고 있다는 게 느껴져요. 아기가 언제나 밝고 건강하게 자라기를 바라요.', NULL, '2024-11-20 13:15:14.989426', NULL, '손끝으로 만지기');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (299, 109, '2024-11-12', NULL, NULL, '오늘 아기가 또 한 걸음 더 나아갔어요! 조금씩 혼자서 걷기 시작하는 모습이 정말 기쁘고 신기했어요. 아기가 혼자서 일어나는 걸 보고 있자니, 성장하는 게 느껴지고, 너무 뿌듯해요. 하루하루 아기가 자라는 모습이 자랑스럽고, 앞으로 더 많은 걸 배울 아기를 지켜보는 게 너무 기대돼요.', NULL, '2024-11-20 13:18:05.722347', NULL, '아기의 첫 걸음');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (300, 109, '2024-11-13', NULL, NULL, '오늘 아기가 새로운 장난감을 가지고 놀았어요. 그 장난감을 쥐고 흔들면서 아기의 얼굴에 미소가 떠오르는 모습을 보고 너무 행복했어요. 아기가 놀이를 통해 많은 것을 배우는 모습이 너무 귀엽고, 이렇게 자주 반응하는 아기를 보면 정말 감사하다는 생각이 듭니다.', NULL, '2024-11-20 13:18:29.090347', NULL, '새로운 장난감');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (302, 109, '2024-11-15', NULL, NULL, '아기가 오늘 처음으로 기어다니기 시작했어요! 처음에는 엉덩이를 밀면서 움직였지만, 조금씩 손과 무릎을 사용해 기어가고 있더라고요. 점점 더 능숙해지면서 주변을 탐색하는 모습을 보니 정말 기뻤어요. 아기가 이렇게 자주 움직이게 되면서 더욱 활발하게 변해가는 모습을 보니, 너무 감사하고 소중해요.', NULL, '2024-11-20 13:19:05.022188', NULL, '기어다니기');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (303, 109, '2024-11-16', NULL, NULL, '오늘 아기가 엄마가 가리킨 물건을 손가락으로 가리키려고 했어요! 아직 정확히 가리진 못하지만, 손을 뻗으면서 무언가를 가리키려는 모습이 정말 귀여웠어요. 아기가 점점 더 많은 걸 배우고, 표현하려고 하는 모습을 보니 기쁘고 뿌듯합니다. 이제 아기와 함께 세상을 더 많이 탐험할 수 있을 것 같아서 너무 기대돼요.', NULL, '2024-11-20 13:19:52.877375', NULL, '손가락으로 가리기');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (304, 109, '2024-11-17', NULL, NULL, '오늘 아기가 처음으로 장난감 자동차를 가지고 놀았어요. 손으로 밀면서 자동차가 움직이는 걸 보고 정말 즐거워했어요. 아기가 이렇게 놀이를 통해 세상과 소통하는 모습을 보니 너무 기쁘고 자랑스러워요. 앞으로도 다양한 장난감을 주며 아기의 상상력과 호기심을 키워주고 싶어요. 이런 소소한 순간들이 아기와의 소중한 기억으로 남을 것 같아요.', NULL, '2024-11-20 13:20:11.172527', NULL, '첫 번째 장난감 자동차');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (293, 109, '2024-11-06', NULL, NULL, '오늘은 아기가 스스로 첫 걸음을 내디뎠어요! 몇 걸음이었지만, 그 작은 발걸음 하나하나가 너무 감동적이에요. 아기가 혼자서 일어나려고 할 때마다 옆에서 도와주려고 했는데, 오늘은 스스로 일어나서 한 걸음 떼더라고요. 이제 걸음마를 배우기 시작하는 것 같아서 앞으로가 너무 기대돼요. 이런 변화들을 겪을 때마다 하루하루가 특별하게 느껴져요.', NULL, '2024-11-20 13:14:44.571712', NULL, '첫 걸음마');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (295, 109, '2024-11-08', NULL, NULL, '아기가 오늘 처음으로 책을 보았어요. 엄마가 책을 읽어주니까 아기가 페이지를 넘기려고 하고, 그림을 손으로 만지며 관심을 보였어요. 이렇게 책에 관심을 가지는 걸 보니 정말 기뻤어요. 이제 아기가 이야기 속에 빠져드는 걸 보고 싶어요. 매일 조금씩 새로운 것을 배우고 있는 아기를 보면, 시간이 너무 빨리 가는 것 같아요.

', NULL, '2024-11-20 13:15:05.713314', NULL, '아기의 첫 책 읽기');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (297, 109, '2024-11-10', NULL, NULL, '오늘 아기가 내가 부른 노래에 맞춰 몸을 흔들었어요! 처음에는 그냥 내내 가만히 있었는데, 나중에는 음악에 맞춰서 몸을 흔드는 모습이 너무 귀엽더라고요. 아기가 어떤 소리에 반응하고, 그 소리에 맞춰 즐거워하는 걸 보면 너무 기뻐요. 아기가 더 많이 웃고, 많이 배워가길 바라요. 노래도 자주 불러줘야겠어요.', NULL, '2024-11-20 13:15:23.971315', NULL, '좋아하는 노래');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (301, 109, '2024-11-14', NULL, NULL, '오늘 아기가 내가 말하는 걸 들으면서 "아!" 하고 대답하는 것 같았어요. 처음엔 내가 말하는 걸 그냥 듣고만 있었는데, 오늘은 뭔가 의사소통을 하려는 듯한 느낌이 들어서 너무 기뻤어요. 이제 아기와 대화할 수 있는 날이 멀지 않았다는 생각에 너무 설렙니다. 점점 더 많은 걸 배우는 아기가 너무 자랑스러워요.', NULL, '2024-11-20 13:18:46.483317', NULL, '첫 번째 대화 시도');
INSERT INTO public.record (record_id, child_id, record_date, height, weight, content, video_result, created_at, updated_at, title) VALUES (308, 108, '2025-01-07', 75.0, 10.0, '맛집이네요', NULL, '2025-01-07 20:16:25.25609', NULL, '맛있어요');


--
-- TOC entry 4360 (class 0 OID 16815)
-- Dependencies: 225
-- Data for Name: refresh_tokens; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.refresh_tokens (user_id, token, expiry_date) VALUES (19, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxOSIsImlhdCI6MTczMjg1MDkzMiwiZXhwIjoxNzMzNDU1NzMyfQ.nHA29zXvmHlp6I1iQRTz_rUwHFFrU4h7RNf3aLV9Rj4', '2024-12-06 03:28:52.150575');
INSERT INTO public.refresh_tokens (user_id, token, expiry_date) VALUES (23, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMyIsImlhdCI6MTczMjAyNDQ4NCwiZXhwIjoxNzMyNjI5Mjg0fQ.yvd82hFt91tqdKha_-gfntwpV1UCboPCHs_6pqlSbG8', '2024-11-26 13:54:44.514987');
INSERT INTO public.refresh_tokens (user_id, token, expiry_date) VALUES (34, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNCIsImlhdCI6MTczNjEyNjQwOCwiZXhwIjoxNzM2NzMxMjA4fQ.bSZOl2F6i63UvN_YlN23Nx5mk0dwZQKLRgn8BtlBECU', '2025-01-13 01:20:08.273243');
INSERT INTO public.refresh_tokens (user_id, token, expiry_date) VALUES (35, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzNSIsImlhdCI6MTczODI1NDI0NSwiZXhwIjoxNzM4ODU5MDQ1fQ.OuI67pokNpxCg1GJog0G_GJnzVgAYEnB_1VcCu1e1Iw', '2025-02-06 16:24:05.325106');
INSERT INTO public.refresh_tokens (user_id, token, expiry_date) VALUES (32, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMiIsImlhdCI6MTczODY1MjEzMiwiZXhwIjoxNzM5MjU2OTMyfQ._3wZLFtLkFutuRZVXplqfMVCcLWmOU_SGZq9_2ZjeII', '2025-02-11 06:55:32.954115');
INSERT INTO public.refresh_tokens (user_id, token, expiry_date) VALUES (21, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMSIsImlhdCI6MTczODczMjI3MSwiZXhwIjoxNzM5MzM3MDcxfQ.89lmN2fl8xHcU6x3mXrEIoSaMF9R8iFAn3dsU_vqjQ0', '2025-02-12 05:11:11.851831');
INSERT INTO public.refresh_tokens (user_id, token, expiry_date) VALUES (27, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNyIsImlhdCI6MTczMjEyNDYyOCwiZXhwIjoxNzMyNzI5NDI4fQ.BVtBSyNTQZd-gWxm6vv2PLdjGKRmybcTIj96p2Ubnq4', '2024-11-27 17:43:48.879485');
INSERT INTO public.refresh_tokens (user_id, token, expiry_date) VALUES (33, 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMyIsImlhdCI6MTczMjUxOTE4NiwiZXhwIjoxNzMzMTIzOTg2fQ.jpRErhCz7QKHuc0SMLqPkZnEq28pD8bzEBSH7dfDg2I', '2024-12-02 07:19:46.677631');


--
-- TOC entry 4351 (class 0 OID 16661)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users (user_id, kakao_id, created_at, updated_at, email) VALUES (19, 3770317930, '2024-11-05 13:42:49.618292', NULL, 'okbsm11@naver.com');
INSERT INTO public.users (user_id, kakao_id, created_at, updated_at, email) VALUES (21, 3778208963, '2024-11-05 13:51:05.610239', NULL, 'legend3569@gmail.com');
INSERT INTO public.users (user_id, kakao_id, created_at, updated_at, email) VALUES (23, 3778118139, '2024-11-05 14:57:40.040368', NULL, 'kjn0406@naver.com');
INSERT INTO public.users (user_id, kakao_id, created_at, updated_at, email) VALUES (27, 3768687319, '2024-11-14 21:34:33.359729', NULL, 'rkdgusrndla@naver.com');
INSERT INTO public.users (user_id, kakao_id, created_at, updated_at, email) VALUES (32, 3779441941, '2024-11-15 11:18:56.044943', NULL, 'qjqmti0113@naver.com');
INSERT INTO public.users (user_id, kakao_id, created_at, updated_at, email) VALUES (33, 3799077438, '2024-11-19 08:51:38.397811', NULL, 'yopy0817@naver.com');
INSERT INTO public.users (user_id, kakao_id, created_at, updated_at, email) VALUES (34, 3865879949, '2025-01-06 01:20:08.210064', NULL, 'jessic32@naver.com');
INSERT INTO public.users (user_id, kakao_id, created_at, updated_at, email) VALUES (35, 3899950716, '2025-01-30 16:24:05.268768', NULL, 'ssacsso@naver.com');


--
-- TOC entry 4359 (class 0 OID 16728)
-- Dependencies: 224
-- Data for Name: weekly_report; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.weekly_report (report_id, child_id, risk_level, feedback, created_at, is_read, video_result_count) VALUES (38, 72, 2, '샘플 피드백입니다.', '2024-11-11 00:01:01.846', true, NULL);
INSERT INTO public.weekly_report (report_id, child_id, risk_level, feedback, created_at, is_read, video_result_count) VALUES (42, 83, 2, '샘플 피드백입니다.', '2024-11-11 08:43:29.962', true, NULL);
INSERT INTO public.weekly_report (report_id, child_id, risk_level, feedback, created_at, is_read, video_result_count) VALUES (43, 83, 2, '샘플 피드백입니다.', '2024-11-18 07:03:56.668616', true, NULL);
INSERT INTO public.weekly_report (report_id, child_id, risk_level, feedback, created_at, is_read, video_result_count) VALUES (41, 98, 2, '샘플 피드백입니다.', '2024-11-10 21:27:44.536', true, 0.0000);
INSERT INTO public.weekly_report (report_id, child_id, risk_level, feedback, created_at, is_read, video_result_count) VALUES (45, 102, 5, '아기의 발달과정은 모든 아이마다 다를 수 있어요. 아이가 느긋하게 성장하고 있으니 너무 걱정하지 않으셔도 돼요. 아이가 손뼉을 치거나 음식을 잘 먹는 모습은 축하해 주면서 여유롭게 기다려주세요. 아이와 눈을 마주치는 시간은 서서히 조금씩 늘려보고, 아이가 자연스럽게 성장하도록 도와주세요.', '2024-11-11 00:01:01.846', false, NULL);
INSERT INTO public.weekly_report (report_id, child_id, risk_level, feedback, created_at, is_read, video_result_count) VALUES (46, 109, 2, '아이의 발달 과정은 정말 아름다운 것 같아요. 아기가 처음으로 손뼉을 치거나 새로운 장난감을 만져보는 모습은 부모님에게도 큰 성취감을 주죠. 앞으로도 아이가 건강하게 자라는 과정을 함께 응원하며 천천히 돌봐주세요.', '2024-11-11 00:01:01.846', false, NULL);
INSERT INTO public.weekly_report (report_id, child_id, risk_level, feedback, created_at, is_read, video_result_count) VALUES (44, 102, 5, '아기의 발달 속도는 개인 차이가 크기 때문에, 다른 아기들과 비교하지 않아도 돼요. 아기가 조용한 모습을 보이더라도, 천천히 자극에 익숙해지며 성장하는 과정일 수 있답니다. 아기가 조금 더 시간을 갖도록 기다려주시고, 아기가 편안하게 발달할 수 있도록 따뜻한 보살핌을 계속해 주세요. 부모님의 사랑과 관심이 아기의 모든 모습을 가장 아름답게 만들어 줄 거예요.', '2024-11-18 00:01:01.846', true, NULL);
INSERT INTO public.weekly_report (report_id, child_id, risk_level, feedback, created_at, is_read, video_result_count) VALUES (49, 109, 2, '아기가 매 순간 성장하는 모습을 지켜보는 일은 정말 특별한 일입니다. 아기가 처음으로 쓰러지고 일어서거나, 새로운 놀이를 통해 세상과 소통하는 경험을 하면서 엄마의 눈빛은 여러분의 자부심과 기쁨으로 반짝입니다. 아기가 점점 더 많은 것을 배우며, 능숙해져가는 모습을 지켜보는 것만으로도 충분히 소중한 순간이 됩니다. 또한, 아기의 발달에 관심을 가지며 주변 환경과의 상호작용을 통해 개인의 잠재력을 발휘할 수 있도록 성장시키는 것은 부모님의 따뜻한 사랑과 배려가 듬뿍한 가정환경에서 이루어질 수 있을 겁니다. 아기와 함께하는 이 순간들이 항상 따뜻한 기억으로 간직되길 바라요.', '2024-11-18 00:01:01.207', true, NULL);


--
-- TOC entry 4376 (class 0 OID 0)
-- Dependencies: 226
-- Name: age_gender_statistics_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.age_gender_statistics_id_seq', 512, true);


--
-- TOC entry 4377 (class 0 OID 0)
-- Dependencies: 217
-- Name: children_child_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.children_child_id_seq', 110, true);


--
-- TOC entry 4378 (class 0 OID 0)
-- Dependencies: 221
-- Name: photo_photo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.photo_photo_id_seq', 255, true);


--
-- TOC entry 4379 (class 0 OID 0)
-- Dependencies: 219
-- Name: record_record_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.record_record_id_seq', 308, true);


--
-- TOC entry 4380 (class 0 OID 0)
-- Dependencies: 215
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.users_user_id_seq', 35, true);


--
-- TOC entry 4381 (class 0 OID 0)
-- Dependencies: 223
-- Name: weekly_report_report_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.weekly_report_report_id_seq', 57, true);


--
-- TOC entry 4201 (class 2606 OID 16876)
-- Name: age_gender_statistics age_gender_statistics_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.age_gender_statistics
    ADD CONSTRAINT age_gender_statistics_pkey PRIMARY KEY (age_gender_statistics_id);


--
-- TOC entry 4185 (class 2606 OID 16693)
-- Name: children children_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.children
    ADD CONSTRAINT children_pkey PRIMARY KEY (child_id);


--
-- TOC entry 4187 (class 2606 OID 16862)
-- Name: record idx_record; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.record
    ADD CONSTRAINT idx_record UNIQUE (child_id, record_id);


--
-- TOC entry 4193 (class 2606 OID 16721)
-- Name: photo photo_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (photo_id);


--
-- TOC entry 4197 (class 2606 OID 16821)
-- Name: refresh_tokens pk_refresh_tokens; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT pk_refresh_tokens PRIMARY KEY (user_id);


--
-- TOC entry 4190 (class 2606 OID 16708)
-- Name: record record_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.record
    ADD CONSTRAINT record_pkey PRIMARY KEY (record_id);


--
-- TOC entry 4199 (class 2606 OID 16823)
-- Name: refresh_tokens refresh_tokens_token_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_token_key UNIQUE (token);


--
-- TOC entry 4181 (class 2606 OID 16669)
-- Name: users users_kakao_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_kakao_id_key UNIQUE (kakao_id);


--
-- TOC entry 4183 (class 2606 OID 16783)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4195 (class 2606 OID 16734)
-- Name: weekly_report weekly_report_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.weekly_report
    ADD CONSTRAINT weekly_report_pkey PRIMARY KEY (report_id);


--
-- TOC entry 4191 (class 1259 OID 16880)
-- Name: idx_photo_record_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_photo_record_id ON public.photo USING btree (record_id);


--
-- TOC entry 4188 (class 1259 OID 16879)
-- Name: idx_record_child_id_date; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_record_child_id_date ON public.record USING btree (child_id, record_date);


--
-- TOC entry 4203 (class 2606 OID 16709)
-- Name: record fk_child_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.record
    ADD CONSTRAINT fk_child_id FOREIGN KEY (child_id) REFERENCES public.children(child_id) ON DELETE CASCADE;


--
-- TOC entry 4205 (class 2606 OID 16735)
-- Name: weekly_report fk_child_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.weekly_report
    ADD CONSTRAINT fk_child_id FOREIGN KEY (child_id) REFERENCES public.children(child_id) ON DELETE CASCADE;


--
-- TOC entry 4204 (class 2606 OID 16722)
-- Name: photo fk_record_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.photo
    ADD CONSTRAINT fk_record_id FOREIGN KEY (record_id) REFERENCES public.record(record_id) ON DELETE CASCADE;


--
-- TOC entry 4206 (class 2606 OID 16824)
-- Name: refresh_tokens fk_refresh_tokens_user; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


--
-- TOC entry 4202 (class 2606 OID 16784)
-- Name: children fk_user_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.children
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON DELETE CASCADE;


-- Completed on 2025-02-06 00:07:03

--
-- PostgreSQL database dump complete
--

