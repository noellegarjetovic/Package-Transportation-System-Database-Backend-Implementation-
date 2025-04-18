USE [projekatSab]
GO
/****** Object:  StoredProcedure [dbo].[SPgrantRequest]    Script Date: 09/07/2024 03:26:53 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create PROCEDURE [dbo].[SPgrantRequest]
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
