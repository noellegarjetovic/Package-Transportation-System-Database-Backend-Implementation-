USE [projekatSab]
GO
/****** Object:  Table [dbo].[Administratori]    Script Date: 09/07/2024 03:20:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Administratori](
	[KorIme] [nvarchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[KorIme] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Grad]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Grad](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Naziv] [nvarchar](100) NOT NULL,
	[PostanskiBroj] [nvarchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UQ_Grad_Naziv] UNIQUE NONCLUSTERED 
(
	[Naziv] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UQ_Grad_PostanskiBroj] UNIQUE NONCLUSTERED 
(
	[PostanskiBroj] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Korisnik]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Korisnik](
	[KorIme] [nvarchar](100) NOT NULL,
	[Ime] [nvarchar](100) NOT NULL,
	[Prezime] [nvarchar](100) NOT NULL,
	[Sifra] [nvarchar](100) NOT NULL,
	[BrojPoslatih] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[KorIme] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Kuriri]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Kuriri](
	[KorIme] [nvarchar](100) NOT NULL,
	[RegistarskiBroj] [nvarchar](100) NULL,
	[BrojIsporucenih] [int] NULL,
	[OstvareniProfit] [decimal](10, 3) NULL,
	[Status] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[KorIme] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Opstina]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Opstina](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Naziv] [nvarchar](100) NOT NULL,
	[GradId] [int] NOT NULL,
	[X] [decimal](10, 3) NOT NULL,
	[Y] [decimal](10, 3) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Paket]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Paket](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[KorIme] [nvarchar](100) NOT NULL,
	[OpstinaP] [int] NOT NULL,
	[OpstinaD] [int] NOT NULL,
	[Tip] [int] NOT NULL,
	[Tezina] [decimal](10, 3) NOT NULL,
	[KurirKorIme] [nvarchar](100) NULL,
	[StatusIsporuke] [int] NOT NULL,
	[Cena] [decimal](10, 3) NULL,
	[VremeP] [datetime] NULL,
 CONSTRAINT [PK__Paket__3214EC07819CA844] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Ponuda]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Ponuda](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Procenat] [decimal](5, 2) NOT NULL,
	[KurirKorIme] [nvarchar](100) NOT NULL,
	[PaketId] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Vozilo]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Vozilo](
	[RegistarskiBroj] [nvarchar](100) NOT NULL,
	[TipGoriva] [int] NOT NULL,
	[Potrosnja] [decimal](10, 3) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[RegistarskiBroj] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ZahteviKuriri]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ZahteviKuriri](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[KorIme] [nvarchar](100) NOT NULL,
	[RegistarskiBroj] [nvarchar](100) NOT NULL,
 CONSTRAINT [PK__ZahteviK__3214EC07DFD36497] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UQ_ZahteviKuriri_KorIme] UNIQUE NONCLUSTERED 
(
	[KorIme] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Korisnik] ADD  DEFAULT ((0)) FOR [BrojPoslatih]
GO
ALTER TABLE [dbo].[Kuriri] ADD  DEFAULT ((0)) FOR [BrojIsporucenih]
GO
ALTER TABLE [dbo].[Kuriri] ADD  DEFAULT ((0)) FOR [OstvareniProfit]
GO
ALTER TABLE [dbo].[Kuriri] ADD  DEFAULT ((0)) FOR [Status]
GO
ALTER TABLE [dbo].[Administratori]  WITH CHECK ADD FOREIGN KEY([KorIme])
REFERENCES [dbo].[Korisnik] ([KorIme])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Kuriri]  WITH CHECK ADD FOREIGN KEY([KorIme])
REFERENCES [dbo].[Korisnik] ([KorIme])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Kuriri]  WITH CHECK ADD FOREIGN KEY([RegistarskiBroj])
REFERENCES [dbo].[Vozilo] ([RegistarskiBroj])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[Opstina]  WITH CHECK ADD FOREIGN KEY([GradId])
REFERENCES [dbo].[Grad] ([Id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[Paket]  WITH CHECK ADD  CONSTRAINT [FK__Paket__KorIme__6477ECF3] FOREIGN KEY([KorIme])
REFERENCES [dbo].[Korisnik] ([KorIme])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Paket] CHECK CONSTRAINT [FK__Paket__KorIme__6477ECF3]
GO
ALTER TABLE [dbo].[Paket]  WITH CHECK ADD  CONSTRAINT [FK__Paket__KurirKorI__6754599E] FOREIGN KEY([KurirKorIme])
REFERENCES [dbo].[Korisnik] ([KorIme])
GO
ALTER TABLE [dbo].[Paket] CHECK CONSTRAINT [FK__Paket__KurirKorI__6754599E]
GO
ALTER TABLE [dbo].[Paket]  WITH CHECK ADD  CONSTRAINT [FK__Paket__OpstinaD__66603565] FOREIGN KEY([OpstinaD])
REFERENCES [dbo].[Opstina] ([Id])
GO
ALTER TABLE [dbo].[Paket] CHECK CONSTRAINT [FK__Paket__OpstinaD__66603565]
GO
ALTER TABLE [dbo].[Paket]  WITH CHECK ADD  CONSTRAINT [FK__Paket__OpstinaP__656C112C] FOREIGN KEY([OpstinaP])
REFERENCES [dbo].[Opstina] ([Id])
GO
ALTER TABLE [dbo].[Paket] CHECK CONSTRAINT [FK__Paket__OpstinaP__656C112C]
GO
ALTER TABLE [dbo].[Ponuda]  WITH CHECK ADD FOREIGN KEY([KurirKorIme])
REFERENCES [dbo].[Korisnik] ([KorIme])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Ponuda]  WITH CHECK ADD  CONSTRAINT [FK__Ponuda__PaketId__76969D2E] FOREIGN KEY([PaketId])
REFERENCES [dbo].[Paket] ([Id])
GO
ALTER TABLE [dbo].[Ponuda] CHECK CONSTRAINT [FK__Ponuda__PaketId__76969D2E]
GO
ALTER TABLE [dbo].[ZahteviKuriri]  WITH CHECK ADD  CONSTRAINT [FK__ZahteviKu__KorIm__4AB81AF0] FOREIGN KEY([KorIme])
REFERENCES [dbo].[Korisnik] ([KorIme])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ZahteviKuriri] CHECK CONSTRAINT [FK__ZahteviKu__KorIm__4AB81AF0]
GO
ALTER TABLE [dbo].[ZahteviKuriri]  WITH CHECK ADD  CONSTRAINT [FK__ZahteviKu__Regis__4BAC3F29] FOREIGN KEY([RegistarskiBroj])
REFERENCES [dbo].[Vozilo] ([RegistarskiBroj])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[ZahteviKuriri] CHECK CONSTRAINT [FK__ZahteviKu__Regis__4BAC3F29]
GO
/****** Object:  StoredProcedure [dbo].[SPgrantRequest]    Script Date: 09/07/2024 03:20:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[SPgrantRequest]
    @Id INT
AS
BEGIN
    DECLARE @KorIme NVARCHAR(100);
    DECLARE @RegistarskiBroj NVARCHAR(100);

    
    SELECT @KorIme = KorIme, @RegistarskiBroj = RegistarskiBroj
    FROM dbo.ZahteviKuriri
    WHERE Id = @Id;

    
    IF @KorIme IS NULL OR @RegistarskiBroj IS NULL
    BEGIN
        RAISERROR('Zahtev ne postoji.', 16, 1);
        RETURN;
    END

    
   
        
        IF NOT EXISTS (SELECT KorIme FROM dbo.Korisnik WHERE KorIme = @KorIme)
        BEGIN
            RAISERROR('Korisnik ne postoji.', 16, 1);
            RETURN;
        END

        
        IF EXISTS (SELECT KorIme FROM dbo.Kuriri WHERE KorIme = @KorIme)
        BEGIN
            RAISERROR('Korisnik je već kurir.', 16, 1);
            RETURN;
        END

        
        IF NOT EXISTS (SELECT RegistarskiBroj FROM dbo.Vozilo WHERE RegistarskiBroj = @RegistarskiBroj)
        BEGIN
            RAISERROR('Vozilo ne postoji.', 16, 1);
            RETURN;
        END

        
        INSERT INTO Kuriri (KorIme, RegistarskiBroj, BrojIsporucenih, OstvareniProfit, Status)
        VALUES (@KorIme, @RegistarskiBroj, 0, 0, 0);
    

    
    DELETE FROM dbo.ZahteviKuriri WHERE Id = @Id;
END;
GO

USE [projekatSab]
GO
/****** Object:  Trigger [dbo].[BrisanjePonuda]    Script Date: 09/07/2024 03:24:12 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

create TRIGGER [dbo].[BrisanjePonuda]
ON [dbo].[Paket]
AFTER UPDATE
AS
BEGIN
    IF UPDATE(StatusIsporuke)
    BEGIN
        DECLARE @PaketId INT;
        DECLARE @cursor CURSOR;

        SET @cursor = CURSOR FOR
        SELECT id FROM inserted WHERE StatusIsporuke = 1;

        OPEN @cursor;
        FETCH NEXT FROM @cursor INTO @PaketId;

        WHILE @@FETCH_STATUS = 0
        BEGIN
            DELETE FROM Ponuda
            WHERE PaketId = @PaketId;

            FETCH NEXT FROM @cursor INTO @PaketId;
        END

        CLOSE @cursor;
        DEALLOCATE @cursor;
    END
END;
