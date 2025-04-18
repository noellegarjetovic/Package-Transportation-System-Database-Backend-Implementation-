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
