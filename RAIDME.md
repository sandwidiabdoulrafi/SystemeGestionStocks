"Application de gestion de stock avec Java." 
Avant de lancer l'application, voici les prérequis nécessaires :

   1- Java Development Kit (JDK) :
        Installez Java JDK 8 ou une version plus récente sur votre système.
        Assurez-vous que la variable d'environnement JAVA_HOME est correctement configurée.

   2- Base de données PostgreSQL :
        Installez PostgreSQL sur votre système (version 9.6 ou ultérieure recommandée).
        Créez une base de données nommée "gestion_reservations_voyages".
        Créez un utilisateur avec les droits nécessaires sur cette base de données.

   3-Driver JDBC pour PostgreSQL :
        Téléchargez le driver JDBC PostgreSQL (postgresql-42.x.x.jar) depuis le site officiel de PostgreSQL.
        Placez ce fichier .jar dans le répertoire "lib/" de votre projet.

   4- Configuration de la base de données :
        Dans le fichier src/donnees/ConnexionBaseDeDonnees.java, mettez à jour les informations de connexion :

    private static final String URL = "jdbc:postgresql://localhost:5432/gestion_reservations_voyages";
    private static final String UTILISATEUR = "votre_utilisateur";
    private static final String MOT_DE_PASSE = "votre_mot_de_passe";

   5-Création des tables dans la base de données :

    Exécutez les requêtes SQL suivantes pour créer le-- Table des agences de voyage
  -- Table: public.passagers

-- DROP TABLE IF EXISTS public.passagers;

CREATE TABLE IF NOT EXISTS public.passagers
(
    id integer NOT NULL DEFAULT nextval('passagers_id_seq'::regclass),
    nom character varying(100) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    numero_passeport character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT passagers_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.passagers
    OWNER to postgres;

-- Table: public.vols

-- DROP TABLE IF EXISTS public.vols;

CREATE TABLE IF NOT EXISTS public.vols
(
    numero_vol character varying(10) COLLATE pg_catalog."default" NOT NULL,
    date_depart timestamp without time zone NOT NULL,
    date_arrivee timestamp without time zone NOT NULL,
    destination character varying(100) COLLATE pg_catalog."default" NOT NULL,
    places_disponibles integer NOT NULL,
    prix numeric(10,2) NOT NULL,
    CONSTRAINT vols_pkey PRIMARY KEY (numero_vol)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.vols
    OWNER to postgres;

-- Table: public.reservations

-- DROP TABLE IF EXISTS public.reservations;

CREATE TABLE IF NOT EXISTS public.reservations
(
    id integer NOT NULL DEFAULT nextval('reservations_id_seq'::regclass),
    passager_id integer NOT NULL,
    vol_id character varying(10) COLLATE pg_catalog."default" NOT NULL,
    statut character varying(20) COLLATE pg_catalog."default" NOT NULL,
    agence_id integer NOT NULL, -- Ajout de la référence à l'agence
    CONSTRAINT reservations_pkey PRIMARY KEY (id),
    CONSTRAINT reservations_passager_id_fkey FOREIGN KEY (passager_id)
        REFERENCES public.passagers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT reservations_vol_id_fkey FOREIGN KEY (vol_id)
        REFERENCES public.vols (numero_vol) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT reservations_agence_id_fkey FOREIGN KEY (agence_id)
        REFERENCES public.agences_voyage (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.reservations
    OWNER to postgres;

-- Table: public.reservation_passagers

-- DROP TABLE IF EXISTS public.reservation_passagers;

CREATE TABLE IF NOT EXISTS public.reservation_passagers
(
    reservation_id integer NOT NULL,
    passager_id integer NOT NULL,
    CONSTRAINT reservation_passagers_pkey PRIMARY KEY (reservation_id, passager_id),
    CONSTRAINT reservation_passagers_passager_id_fkey FOREIGN KEY (passager_id)
        REFERENCES public.passagers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT reservation_passagers_reservation_id_fkey FOREIGN KEY (reservation_id)
        REFERENCES public.reservations (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.reservation_passagers
    OWNER to postgres;

-- Table: public.agences_voyage

-- DROP TABLE IF EXISTS public.agences_voyage;

CREATE TABLE IF NOT EXISTS public.agences_voyage
(
    id integer NOT NULL DEFAULT nextval('agences_voyage_id_seq'::regclass),
    nom character varying(100) COLLATE pg_catalog."default" NOT NULL,
    adresse character varying(200) COLLATE pg_catalog."default" NOT NULL,
    telephone character varying(20) COLLATE pg_catalog."default" NOT NULL,
    offres_speciales text COLLATE pg_catalog."default",
    CONSTRAINT agences_voyage_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.agences_voyage
    OWNER to postgres;

    



   6- Configuration de JDBC pour IntelliJ IDEA :
        Assurez-vous que le driver JDBC PostgreSQL (postgresql-42.x.x.jar) est inclus dans le lib/ de votre projet.
        Le fichier JAR du pilote JDBC de la base de données (disponible sur le site officiel de la base de données) et ajoute-le manuellement à ton projet.
	•Clique droit sur ton projet dans IntelliJ > Open Module Settings >Dependencies> Libraries > + > Choisis le fichier .jar.

   

    7- Environnement de développement :
        Utilisez un IDE comme Eclipse, IntelliJ IDEA ou NetBeans pour faciliter le développement et l'exécution de l'application.
        Configurez le projet dans votre IDE en incluant tous les fichiers source et le driver JDBC dans le classpath.

  

    8- Compilation :
        Compilez tous les fichiers source Java dans le répertoire "src/".

Une fois ces prérequis satisfaits, vous devriez être en mesure de lancer l'application en exécutant la classe Main.java.

N'oubliez pas de gérer les exceptions et d'ajouter des journalisations (logging) appropriées pour faciliter le débogage en cas de problèmes lors de l'exécution.
