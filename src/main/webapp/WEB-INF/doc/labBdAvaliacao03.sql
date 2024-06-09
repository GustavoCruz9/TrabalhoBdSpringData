/*
use master
go
drop database labBdAvaliacao03
*/
create database labBdAvaliacao03
go
use labBdAvaliacao03
go
--PROCEDURE QUE VALIDA SE O CPF EXISTE OU É INVALIDO
--drop procedure sp_consultaCpf
create procedure sp_consultaCpf(@cpf char(11), @valido bit output)
as
--VARIAVEIS
	declare @i int, @valor int, @status int, @x int
--VALORES DAS VARIAVEIS
	set @i = 0
	set @status = 0
	set @x = 2

--verifica se cpf tem 11 digitos
if(LEN(@cpf) = 11)begin
	--VERIFICACAO DE DIGITOS REPETIDOS
	while(@i < 10) begin
		if(SUBSTRING(@cpf, 1,1) = SUBSTRING(@cpf, @x, 1)) begin
			set @status = @status + 1
		end 
	set @i = @i + 1
	set @x = @x + 1
	end
	--Descobrindo o digito 10
	If(@status < 10)begin
		declare @ValorMultiplicadoPor2 int
		set @valor = 10
		set @i = 0
		set @x = 1
		set @ValorMultiplicadoPor2 = 0
		
		while (@i < 9) begin
			set @ValorMultiplicadoPor2 = CAST(SUBSTRING(@cpf, @x, 1) as int) * @valor + @ValorMultiplicadoPor2  
			set @x = @x + 1
			set @i = @i + 1
			set @valor = @valor - 1
		end
		
		declare @valorDividido int, @primeiroDigito int 

		set @valorDividido = @ValorMultiplicadoPor2 % 11

		if(@valorDividido < 2)begin
			set @primeiroDigito = 0
		end else begin
			set @primeiroDigito = 11 - @valorDividido
		end

		-- verifica se o digito descoberto ? igual o inserido

		if(CAST(SUBSTRING(@cpf, 10,1)as int) = @primeiroDigito) begin
			--descobrindo segundo digito
			set @valor = 11
			set @i = 0
			set @x = 1
			set @ValorMultiplicadoPor2 = 0

			while (@i < 10) begin
			set @ValorMultiplicadoPor2 =  CAST(SUBSTRING(@cpf, @x, 1) as int) * @valor + @ValorMultiplicadoPor2
			set @x = @x + 1
			set @i = @i + 1
			set @valor = @valor - 1
			end
			
			declare @segundoDigito int
			set @valorDividido = @ValorMultiplicadoPor2 % 11

			if(@valorDividido < 2)begin
				set @segundoDigito = 0
			end else begin
				set @segundoDigito = 11 - @valorDividido
			end

			if(CAST(SUBSTRING(@cpf, 11,1)as int) = @segundoDigito) begin
					set @valido  = 1
			end else begin
					set @valido  = 0
					raiserror('CPF inexistente', 16, 1)
			end

		end else begin
			raiserror('CPF inexistente', 16, 1)
		end

	end else begin
		raiserror('CPF invalido, todos os digitos sao iguais', 16, 1)
	end

end else begin
	raiserror('CPF invalido, numero de caracteres incorreto', 16, 1)
end


go
--PROCEDURE QUE VALIDA SE ALUNO TEM 16 ANOS OU MAIS
-- drop procedure sp_validaIdade
create procedure sp_validaIdade(@dataNascimento date, @validaIdade bit output)
as
	if(datediff(year, @dataNascimento, getdate()) >= 16)begin
		set @validaIdade = 1
	end
	else
	begin
		set @validaIdade = 0
		raiserror('A idade é menor que 16 anos', 16, 1)
	end

go
--PROCEDURE QUE CALCULA 5 ANOS DO ANO DE INGRESSSO	
-- drop function fn_anoLimite
create function fn_anoLimite(@anoIngresso int)
returns int
as
begin
		declare @anoLimite int
		set @anolimite = @anoIngresso + 5
		return @anoLimite
end

go
-- Funcao para criacao de RA
-- drop function fn_criaRa
create function fn_criaRa (@anoIngresso int, @semestreIngresso int, @random1 int, @random2 int, @random3 int, @random4 int)
returns @tabela table (
	statusRa	 bit,
	ra			char(9)
)
begin

    declare @ra char(9),
			@raExistente char(9)

	set @raExistente = null

	set @ra = cast(@anoIngresso as char(4)) + cast(@semestreIngresso as char(1)) + cast(@random1 as char(1)) + cast(@random2 as char(1)) + cast(@random3 as char(1)) + cast(@random4 as char(1))	

	set @raExistente = (select ra from Aluno where ra = @ra)

	if(@raExistente is null)
	begin
			insert into @tabela (statusRa, ra) values (1, @ra)
	end
	else
	begin
			insert into @tabela (statusRa, ra) values (0, @ra)
	end
	
    return 
end

go
--FUNCTION QUE CRIA O EMAIL CORPORATIVO
-- Drop function fn_criaEmailCorporativo
create function fn_criaEmailCorporativo(@nome varchar(150), @ra char(9))
returns varchar (100)
as
begin

	set @nome = LOWER(@nome)
	set @nome = REPLACE(@nome, ' ', '.')

	set @nome = @nome + RIGHT(@ra, 4) + '@agis.com'
	return @nome
end

go
--PROCEDURE QUE VALIDA SE CPF é UNICO NO BANCO DE DADOS DO SISTEMA
-- drop procedure sp_validaCpfDuplicado
create procedure sp_validaCpfDuplicado(@cpf char(11), @validaCpfDuplicado bit output)
as
	declare @cpfExistente char(11)

	set @cpfExistente = null

	set @cpfExistente = (select cpf from aluno where cpf = @cpf)

	if(@cpfExistente is null)
	begin
		set @validaCpfDuplicado = 1
	end
	else
	begin
		set @validaCpfDuplicado = 0
	end

go
-- PROCEDURE PARA VERIFICAÇÃO DE RA
-- drop procedure sp_validaRa
create procedure sp_validaRa(@ra char(9), @saida bit output)
as
	declare @raExistente char(9)

	set @raExistente = null

	set @raExistente = (select ra from aluno where ra = @ra)

	if(@raExistente is null)
	begin
		set @saida = 0
	end
	else
	begin
		set @saida = 1
	end

go
-- PROCEDURE QUE VALIDA SE CURSO É EXISTENTE
-- drop procedure sp_validaCurso
create procedure sp_validaCurso(@codCurso int, @validaCurso bit output)
as
	set @codCurso = (select codCurso from Curso where codCurso = @codCurso)

	if(@codCurso is not null)
	begin
		set @validaCurso = 1
	end
	else 
	begin
		set @validaCurso = 0
		raiserror('O codigo do curso é invalido', 16, 1)
	end

go
--PROCEDURE QUE VALIDA SE TELEFONE EXISTE
-- drop procedure sp_validaTelefone
create procedure sp_validaTelefone(@telefone char(11), @validaTelefone bit output)
as
	set @telefone = (select numero from Telefone where numero = @telefone)

	if(@telefone is not null)
	begin
		set @validaTelefone = 1
	end
	else 
	begin
		set @validaTelefone = 0
	end

go
--PROCEDURE PARA INSERIR E ATUALIZAR ALUNO
-- drop procedure sp_iuAluno
create procedure sp_iuAluno(@op char(1), @cpf char(11), @codCurso int, @nome varchar(150), @nomeSocial varchar(150), @dataNascimento date, @email varchar(100), @dataConclusao2Grau date,
							@instituicao2Grau varchar(100), @pontuacaoVestibular int, @posicaoVestibular int, @anoIngresso int, @semestreIngresso int, @semestreLimite int, 
							@saida varchar(100) output)
as	
		declare @validaCpf bit
		exec sp_consultaCpf @cpf, @validaCpf output 
		if(@validaCpf = 1)
		begin
				
				declare @validaIdade bit
				exec sp_validaIdade @dataNascimento, @validaIdade output
				if(@validaIdade = 1)
				begin
							declare @validaCurso bit
							exec sp_validaCurso @codCurso, @validaCurso output
							if(@validaCurso = 1)
							begin		
										if(upper(@op) = 'I')						
										begin

												declare @validarDuplicidadeCpf bit
												exec sp_validaCpfDuplicado @cpf, @validarDuplicidadeCpf output
												if(@validarDuplicidadeCpf = 1)
												begin
															declare	@ra char(9),
																	@emailCorporativo varchar(100),
																	@random1 int,
																	@random2 int, 
																	@random3 int, 
																	@random4 int,
																	@status bit

															set @status = 0

															while(@status = 0)begin
									
																set @random1 = CAST(RAND() * 10 as int)
																set @random2 = CAST(RAND() * 10 as int)
																set @random3 = CAST(RAND() * 10 as int)
																set @random4 = CAST(RAND() * 10 as int)

																set @status = (select statusRa from fn_criaRa(2024, 1, @random1, @random2, @random3, @random4))
								
															end

															set @ra = (select ra from fn_criaRa(2024, 1, @random1, @random2, @random3, @random4))
								

															set @emailCorporativo = (select dbo.fn_criaEmailCorporativo(@nome, @ra) as emailCorporativo)

															declare @anolimite int
															set @anoLimite = (select dbo.fn_anoLimite(@anoIngresso) as anoLimite)
								
															insert into Aluno (cpf, codCurso, ra, nome, nomeSocial, dataNascimento, email, dataConclusao2Grau, emailCorporativo, instituicao2Grau,
																				 pontuacaoVestibular, posicaoVestibular, anoIngresso, semestreIngresso, semestreLimite, anolimite, turno)
															values (@cpf, @codCurso, @ra, @nome, @nomeSocial, @dataNascimento, @email, @dataConclusao2Grau, @emailCorporativo, @instituicao2Grau,
																				 @pontuacaoVestibular, @posicaoVestibular, @anoIngresso, @semestreIngresso, @semestreLimite, @anolimite, 'Vespertino')

											
															set @saida = 'Aluno inserido com sucesso'
															return
												end
												else
												begin
															raiserror('CPF já cadastrado', 16, 1)
															return
												end
										end
										else
											if(upper(@op) = 'U')
											begin
									
													update Aluno
													set nome = @nome, dataNascimento = @dataNascimento, nomeSocial = @nomeSocial, email = @email, codCurso = @codCurso, dataConclusao2Grau = @dataConclusao2Grau, 
													instituicao2Grau = @instituicao2Grau, pontuacaoVestibular = @pontuacaoVestibular, posicaoVestibular = @posicaoVestibular
													where cpf = @cpf

										
													set @saida = 'Aluno atualizado com sucesso'	
													return
											end
											else
											begin
													raiserror('Operação invalida', 16, 1)
													return
											end
							end	
				end	
		end

go
-- FUNCTION PARA OBTER ANOSEMESTRE
-- drop function fn_obterAnoSemestre
create function fn_obterAnoSemestre ()
returns varchar(5)
begin
		declare @anoSemestre varchar(5),
				@ano int,
				@mes int;

		
		set @ano = year(getdate())
		set @mes = month(getdate())


		if @mes >= 1 and @mes <= 6
			set @anoSemestre = cast(@ano as varchar(4)) + '1'
		else
			set @anoSemestre = cast(@ano as varchar(4)) + '2'

		return @anoSemestre
				
end

go
-- trigger que insere os auluno em todas materias do primeiro semestre logo apos ele ser inserido
-- disable trigger t_matriculaAluno on Aluno
-- drop trigger t_matriculaAluno
create trigger t_matriculaAluno on Aluno
after insert
as 
begin
		declare @i int,
				@codCurso int,
				@cpf char(11),
				@codDisciplina int,
				@anoSemestre int,
				@codPesoAvaliacao int,
				@tamanhoPeso int

		set @codDisciplina = 0 
		
		set @codPesoAvaliacao = 0 
		
		set @codCurso = (select codCurso from inserted)

		set @i = (select count(codDisciplina) from Disciplina where semestre = 1 and codCurso = @codCurso)

		set @cpf = (select cpf from inserted)

		set @anoSemestre = dbo.fn_obterAnoSemestre()


		create table #disciplinas(
			cod		int 
		)

		insert into #disciplinas (cod) 
			   select codDisciplina from Disciplina where semestre = 1 and codCurso = @codCurso

		create table #pesos(
			cod		int 
		)


		while(@i > 0)
		begin
			
			set @codDisciplina =  (select top 1 cod from #disciplinas)

			insert into Matricula (anoSemestre, cpf, codDisciplina, dataMatricula, statusMatricula) values
			(@anoSemestre, @cpf, @codDisciplina, CONVERT(VARCHAR(10), CAST(getdate() AS DATE), 103), 'pendente')

			insert into #pesos (cod)
				   select codigo from PesoAvaliacao where codDisciplina = @codDisciplina order by tipo asc

			set @tamanhoPeso = (select count(cod) from #pesos)

			while(@tamanhoPeso > 0)
			begin

				set @codPesoAvaliacao =  (select top 1 cod from #pesos)

				insert into Avaliacao (nota, cpf, anoSemestre, codDisciplina, codigoPesoAvaliacao) values 
				(0, @cpf, @anoSemestre, @codDisciplina, @codPesoAvaliacao)

				delete top (1) from #pesos

				set @tamanhoPeso = @tamanhoPeso - 1

			end

			delete top (1) from #disciplinas

			set @i = @i - 1

		end

end

go
-- Procedure que cadastra e atualiza telefone
-- drop procedure sp_iudTelefone
create procedure sp_iudTelefone(@op char(1), @cpf char(11), @telefoneAntigo char(11) null, @telefoneNovo char(11), 
								@saida  varchar(150) output)
as
		declare @validaTelefone bit 

		declare @validarExistenciaCpf bit
		exec sp_validaCpfDuplicado @cpf, @validarExistenciaCpf output

		if(@validarExistenciaCpf = 0) 
		begin
				if(len(@telefoneNovo) = 11)
				begin
						if(upper(@op) = 'U')
						begin
								if(len(@telefoneAntigo) = 11)
								begin
										
										exec sp_validaTelefone @telefoneAntigo, @validaTelefone output
										if(@validaTelefone = 1)
										begin

													update Telefone set numero = @telefoneNovo where cpf = @cpf and numero = @telefoneAntigo

													set @saida = 'Telefone atualizado com sucesso'

													return
										end
										else
										begin
													raiserror('O telefone não existe no banco de dados', 16, 1)
										end
								end
								else
								begin
													raiserror('Tamanho de telefone incorreto', 16, 1)
								end
						end
						
						if(upper(@op) = 'D')
						begin
										exec sp_validaTelefone @telefoneNovo, @validaTelefone output
										if(@validaTelefone = 1)
										begin
														delete Telefone where cpf = @cpf and numero = @telefoneNovo

														set @saida = 'Telefone excluido com sucesso'
														
														return
										end
										else
										begin
														raiserror('O telefone não existe no banco de dados', 16, 1)
										end
						end
								
						if(upper(@op) = 'I')
						begin
									exec sp_validaTelefone @telefoneNovo, @validaTelefone output
									if(@validaTelefone = 0)
									begin
												insert into Telefone (cpf, numero) values (@cpf, @telefoneNovo)

												set @saida = 'Telefone cadastrado com sucesso'

												return
									end
									else
									begin
												raiserror('O telefone ja existe no banco de dados', 16, 1)
									end
						end
						else
						begin
									raiserror('Operação invalida', 16, 1)
						end
				end 
				else
				begin
					raiserror('Tamanho de telefone incorreto', 16, 1)
				end
		end
		else
		begin
				raiserror('O CPF não existe na base de dados do sistema', 16, 1)
		end


go
--FUNCTION FN_POPULARMATRICULA
--drop function fn_popularMatricula
create function fn_popularMatricula(@ra char(9))
returns @tabela table (
	diaSemana	varchar(15),
	codDisciplina	int,
	disciplina	varchar(100),
	horasSemanais	time,
	horaInicio		time,
	horaFinal	time,
	statusMatricula	varchar(20)
)
begin
	
		declare @codCurso int

		set @codCurso = (select codCurso from Aluno where ra = @ra)

		insert into @tabela (diaSemana, codDisciplina, disciplina, horasSemanais, horaInicio, horaFinal, statusMatricula)
					select d.diaSemana, d.codDisciplina, d.nome, d.horasSemanais, convert(varchar(5), d.horaInicio, 108) as horaInicio, convert(varchar(5), d.horaFinal, 108) as horaFinal, 'não matriculado' as statusMatricula
					from Disciplina d left outer join Matricula m on d.codDisciplina = m.codDisciplina
					where m.cpf is null and d.codCurso = @codCurso
	
		insert into @tabela (diaSemana, codDisciplina, disciplina, horasSemanais, horaInicio, horaFinal, statusMatricula)
					select d.diaSemana, d.codDisciplina, d.nome, d.horasSemanais, convert(varchar(5), d.horaInicio, 108) as horaInicio , convert(varchar(5), d.horaFinal, 108) as horaFinal, m.statusMatricula
					from Disciplina d, Matricula m
					left join Matricula m1 on m1.cpf = m.cpf
							  and m1.codDisciplina = m.codDisciplina
							  and m1.anoSemestre > m.anoSemestre
							  and m1.statusMatricula = 'Aprovado'
					where d.codCurso = @codCurso and
						  m.statusMatricula = 'Reprovado'
						  and m1.anoSemestre is null and 
						  d.codDisciplina = m.codDisciplina

		return
		
end
	
go
-- PROCEDURE sp_cadastrarMatricula
-- drop procedure sp_cadastrarMatricula
create procedure sp_cadastrarMatricula(@ra char(9), @codDisciplinaRequerida int, @saida varchar(150) output)
as
		declare @codCurso int,
				@diaSemana varchar(15),
				@horaInicioDisciplinaRequerida time,
				@horaInicioDisciplinaMatriculada time,
				@horaFinalDisciplinaMatriculada time,
				@horaFinalDisciplinaRequerida time,
				@qtdMatricula int,
				@cpf char(11),
				@anoSemestre varchar(5),
				@valida bit

		set @valida = 0

		set @cpf = (select cpf from Aluno where ra = @ra)

		set @codCurso = (select codCurso from Aluno where ra = @ra)

		set @diaSemana = (select diaSemana from Disciplina where codDisciplina = @codDisciplinaRequerida)

		set @horaInicioDisciplinaRequerida = (select horaInicio from Disciplina where codDisciplina = @codDisciplinaRequerida)

		set @horaFinalDisciplinaRequerida = (select horaFinal from Disciplina where codDisciplina = @codDisciplinaRequerida)
									
		set @qtdMatricula = (select count(*) from matricula m, Disciplina d 
							 where lower(m.statusMatricula) = lower('Pendente') 
							 and m.codDisciplina = d.codDisciplina and
							 d.codCurso = @codCurso and d.diaSemana = @diaSemana)

		if (@qtdMatricula = 0)
		begin

			set @anoSemestre = dbo.fn_obterAnoSemestre()

			insert into Matricula (anoSemestre, cpf, codDisciplina, statusMatricula, dataMatricula) values
			(@anoSemestre, @cpf, @codDisciplinaRequerida, 'pendente', CONVERT(VARCHAR(10), CAST(getdate() AS DATE), 103))

			

			set @saida = 'Matricula realizada com sucesso'
		end

		create table #matriculastemp(
			horaInicioDisciplinaMatriculada time,
			horaFinalDisciplinaMatriculada time,
		)
		
		insert into #matriculastemp (horaInicioDisciplinaMatriculada, horaFinalDisciplinaMatriculada)
									SELECT d.horaInicio, d.horaFinal
									FROM matricula m, Disciplina d 
									WHERE LOWER(m.statusMatricula) = LOWER('Pendente') 
									AND d.codCurso = @codCurso 
									AND d.diaSemana = @diaSemana and
									m.codDisciplina = d.codDisciplina

		while(@qtdMatricula > 0)
		begin
			

				set @horaInicioDisciplinaMatriculada = (select top 1 horaInicioDisciplinaMatriculada from #matriculastemp)

				set @horaFinalDisciplinaMatriculada = (select top 1 horaFinalDisciplinaMatriculada from #matriculastemp)

				delete top (1) from #matriculastemp

				if((@horaInicioDisciplinaRequerida not between @horaInicioDisciplinaMatriculada and @horaFinalDisciplinaMatriculada)
				and 
				(@horaFinalDisciplinaRequerida not between @horaInicioDisciplinaMatriculada and @horaFinalDisciplinaMatriculada))
				begin

							set @valida  = 1

				end
				else
				begin
							set @valida = 0
							drop table #matriculastemp
							set @saida = 'Já existe um materia cadastrada nesse intervalo de horario'
							return
				end		

				set @qtdMatricula = @qtdMatricula - 1
				
		end

		if(@valida = 1)
		begin
				set @anoSemestre = dbo.fn_obterAnoSemestre()

				insert into Matricula (anoSemestre, cpf, codDisciplina, statusMatricula, dataMatricula) values
						(@anoSemestre, @cpf, @codDisciplinaRequerida, 'pendente', CONVERT(VARCHAR(10), CAST(getdate() AS DATE), 103))

				set @saida = 'Matricula realizada com sucesso'
		end

go
-- function que faz historico do aluno
-- drop function fn_historico
create function fn_historico (@ra char(9))
returns @historico table(
	ra							char(9),
	nome						varchar(150),
	nomeCurso					varchar(100),
	dataPrimeiraMatricula		date,
	pontuacaoVestibular			int,
	posicaoVestibular			int
)
as
begin
	
	declare @nome						varchar(150),
			@nomeCurso					varchar(100),
			@dataPrimeiraMatricula		date,
			@pontuacaoVestibular		int,
			@posicaoVestibular			int,
			@cpf						char(11)

	set @cpf = (select cpf from Aluno where ra = @ra)
	
	set @nome = (select nome from Aluno where ra = @ra)

	set @nomeCurso = (select c.nome from aluno a, Curso c where a.codCurso = c.codCurso and a.ra = @ra)

	set @dataPrimeiraMatricula = (select top 1 CONVERT(VARCHAR, m.dataMatricula, 103) AS dataMatricula from aluno a, Matricula m where a.cpf = m.cpf and a.cpf = '48715259889' order by m.dataMatricula desc)

	set @pontuacaoVestibular = (select pontuacaoVestibular from Aluno where ra = @ra)

	set @posicaoVestibular = (select posicaoVestibular from Aluno where ra = @ra)

	insert into @historico (ra, nome, nomeCurso, dataPrimeiraMatricula, pontuacaoVestibular, posicaoVestibular)
	values (@ra, @nome, @nomeCurso, @dataPrimeiraMatricula, @pontuacaoVestibular, @posicaoVestibular)

	return

end

go
-- function que faz lista de matriculas aprovadas 
-- drop function fn_matriculaAprovada
create function fn_matriculaAprovada(@ra char(9))
returns @matriculas table (
		codDisciplina		int,
		nomeDisciplina		varchar(100),
		nomeProfessor		varchar(100),
		notaFinal			varchar(10),
		qtdFaltas			int
)
as
begin
		insert into @matriculas (codDisciplina, nomeDisciplina, nomeProfessor, notaFinal, qtdFaltas)
					select d.codDisciplina, d.nome, p.nome,
						   case 
						   when (m.statusMatricula like 'Dispensado') then 'D'
						   else cast(m.nota as varchar(10))
						   end as notaFinal, 
						   (select sum(l.ausencia) 
							from ListaChamada l 
							where l.anoSemestre = m.anoSemestre 
							and l.cpf = m.cpf 
							and l.codDisciplina = m.codDisciplina) as qtdFaltas
					from Disciplina d, professor p, Matricula m 
					where d.codProfessor = p.codProfessor and
						  d.codDisciplina = m.codDisciplina and
						  m.cpf =(select cpf from Aluno where ra = @ra )
						  and (m.statusMatricula like 'Aprovado' or 
						  m.statusMatricula like 'Dispensado')
	return
end

go
-- procedure que insere dispensa
-- drop procedure sp_iDispensa
create procedure sp_iDispensa (@cpf char(11), @codDisciplina int, @instituicao varchar(100) ,@saida varchar(100) output)
as 
		declare @validaCpf bit
		exec sp_consultaCpf @cpf, @validaCpf output 
		if(@validaCpf = 1)
		begin
				declare @validarDuplicidadeCpf bit
				exec sp_validaCpfDuplicado @cpf, @validarDuplicidadeCpf output
				if(@validarDuplicidadeCpf = 0)
				begin
						insert into Dispensa (cpf, codDisciplina, dataDispensa, statusDispensa, instituicao) values 
						(@cpf, @codDisciplina, CONVERT(VARCHAR(10), CAST(getdate() AS DATE), 103), 'em analise', @instituicao)

						set @saida = 'Solicitacao de dispensa enviada para secretaria'
				end
				else
				begin
						raiserror('Cpf nao esta cadastrado', 16, 1)
				end
		end

go
--procedure que atualiza resposta da secretaria e atualiza matricula
-- drop procedure sp_atualizaDispensa
create procedure sp_atualizaDispensa (@ra char(9), @codDisciplina varchar(100), @statusDispensa varchar(20), @saida varchar(100) output)
as 
		declare @cpf char(11),
				@anoSemestre varchar(5),
				@matriuculaExiste int,
				@ano int,
				@mes int

		set @matriuculaExiste = null

		set @cpf = (select cpf from Aluno where ra = @ra)

		update Dispensa 
		set statusDispensa = @statusDispensa 
		where cpf = @cpf and codDisciplina = @codDisciplina

		set @saida = 'Solicitacao indeferida'

		if(@statusDispensa like 'Deferido')
		begin

			set @ano = year(getdate())
			set @mes = month(getdate())

			if (@mes >= 1 and @mes <= 6)
				set @anoSemestre = cast(@ano as varchar(4)) + '1'
			else
				set @anoSemestre = cast(@ano as varchar(4)) + '2'

				set @matriuculaExiste = (select count(*) as matricula
				from Matricula
				where anoSemestre = @anoSemestre
					  and cpf = @cpf
					  and codDisciplina = @codDisciplina)

				if(@matriuculaExiste = 1)
				begin
				
					update Matricula set 
					statusMatricula = 'Dispensado' 
					where anoSemestre = @anoSemestre
						  and cpf = @cpf
						  and codDisciplina = @codDisciplina
					  
				end
				else
				begin

					insert Matricula (anoSemestre, cpf, codDisciplina, statusMatricula, dataMatricula)
					values (@anoSemestre, @cpf, @codDisciplina, 'Dispensado', getdate())

				end

				set @saida = 'Dispensa Deferida e Matricula Dispensada'

		end

go
-- procedure que traz disciplinas que o professor esta matriculado
-- drop procedure sp_validaProfessor
create procedure sp_validaProfessor (@codProfessor int, @saida bit output)
as
	
	declare @professorExiste int,
			@professorPossuDisciplina int
	
	set @professorExiste = 0
	
	set @professorPossuDisciplina = 0

	set @professorExiste = (select Count(codProfessor) from Professor where codProfessor = @codProfessor)
	
	if(@professorExiste = 1)
	begin
			set @professorPossuDisciplina = (select count(codDisciplina) from Disciplina where codProfessor = @codProfessor)

			if(@professorPossuDisciplina > 0)
			begin
				set @saida = 1
			end
			else
			begin
				set @saida = 0
				raiserror('Professor nao leciona nenhuma disciplina', 16, 1)
			end
	end
	else
	begin
		set @saida = 0
		raiserror('O codigo de professor nao existe', 16, 1)
	end

go
-- procedure que cadastra chamada
-- drop procedure sp_cadastraChamada
create procedure sp_cadastraChamada (@dataChamada varchar(10), @anoSemestre int, @cpf char(11), @codDisciplina int, 
										@presencas int, @ausencias int, @aula1 char(1), @aula2 char(1), @aula3 char(1),
										@aula4 char(1))
as
	insert into ListaChamada values
	(@dataChamada, @aula1, @aula2, @aula3, @aula4, @ausencias, @presencas, @cpf, @anoSemestre, @codDisciplina)

go 
-- procedure que traz data das chamadas da disicplina selecionada 
--drop function fn_obterChamadasUnicas
create function fn_obterChamadasUnicas(@codDisciplina int)
returns @tabela table (
    dataChamada       date,
    nome              varchar(100),
    anoSemestre       int,
    cpf               char(11),
    codDisciplina     int
)
as 
begin
    declare @anoSemestre int
    set @anoSemestre = dbo.fn_obterAnoSemestre()

    insert into @tabela (dataChamada, nome, anoSemestre, cpf, codDisciplina)
    select dataChamada, nome, @anoSemestre, cpf, codDisciplina
    from (
        select lc.dataChamada, d.nome, lc.cpf, lc.codDisciplina,
               ROW_NUMBER() over (partition by lc.dataChamada order by lc.dataChamada) as row_num
        from ListaChamada lc
        inner join Disciplina d on lc.codDisciplina = d.codDisciplina
        where lc.anoSemestre = @anoSemestre
          and lc.codDisciplina = @codDisciplina
    ) as subquery
    where row_num = 1

    return
end	

go

-- procedure que atualiza chamada
-- drop procedure sp_atualizaChamada
create procedure sp_atualizaChamada (@presenca int, @ausencia int, @aula1 char(1), @aula2 char(1), 
										@aula3 char(1), @aula4 char(1), @codDisciplina int, @cpf char(11), @dataChamada date)
as
	update ListaChamada
		set presenca = @presenca, ausencia = @ausencia, aula1 = @aula1, aula2 = @aula2, aula3 = @aula3, aula4 = @aula4
	where dataChamada = @dataChamada 
			and anoSemestre = (dbo.fn_obterAnoSemestre()) 
			and codDisciplina = @codDisciplina
			and cpf = @cpf
go
-- procedure para listar Alunos
create procedure sp_listaAlunos
as
	SELECT a.cpf, a.codCurso, a.ra, a.nome, a.nomeSocial, a.dataNascimento, a.email, a.emailCorporativo,
				a.dataConclusao2Grau, a.instituicao2Grau, a.pontuacaoVestibular, a.posicaoVestibular, a.anoIngresso,
				a.semestreIngresso, a.anoIngresso, a.anoLimite, a.semestreLimite,
				(SELECT t1.numero FROM Telefone t1 WHERE t1.cpf = a.cpf AND t1.numero IS NOT NULL ORDER BY t1.numero OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY) AS telefone1,
				(SELECT t2.numero FROM Telefone t2 WHERE t2.cpf = a.cpf AND t2.numero IS NOT NULL ORDER BY t2.numero OFFSET 1 ROWS FETCH NEXT 1 ROW ONLY) AS telefone2
				FROM Aluno a
go

-- Procedure que verifica status de frequencia e notas
-- drop procedure sp_finalizaSemestre
create procedure sp_finalizaSemestre (@codDisciplina int, @anoSemestre int, @saida varchar(100) output)
as			
	declare @cpf char(11),	
			@qtdPesosAvaliacao int,
			@media decimal(4, 2),
			@statusAluno varchar(20),
			@qtdAlunos int,
			@nota float,
			@qtdAulasPorSemana int,
			@qtdChamadas int,
			@horasSemanais varchar(10),
			@horas int,
			@minutos int,
			@totalHoras int,
			@qtdPresencasPossiveis int,
			@qtdPresencasAluno int,
			@frequencia decimal(5,2)

	-- Pega a quantidade de chamadas realizadas

	set @qtdChamadas = (select count(distinct dataChamada) as numeroDeAulas from ListaChamada where codDisciplina = @codDisciplina)

	-- Pega a Quantidade de presenças possiveis por semana
	
	set @horasSemanais = (select horasSemanais from Disciplina where codDisciplina = @codDisciplina)
	set @horas = (cast(substring(@horasSemanais, 1, charindex(':', @horasSemanais) - 1) as int) * 60 )
	set @minutos = cast(substring(@horasSemanais, charindex(':',@horasSemanais ) + 1, len(@horasSemanais)) as int)
	set @totalHoras = @horas + @minutos

	set @qtdAulasPorSemana = (@totalHoras / 50)

	-- Calcula a Quantidade de presenças possiveis no semestre

	set @qtdPresencasPossiveis = (@qtdChamadas * @qtdAulasPorSemana)

	create table #notas(
		nota float
	)

	create table #alunos(
		cpf char(11)
	)

	insert into #alunos (cpf)
				select cpf from Matricula where codDisciplina = @codDisciplina and anoSemestre = @anoSemestre and statusMatricula = 'pendente'

	set @qtdAlunos = (select count(cpf) as qtdAlunos from Matricula where anoSemestre = @anoSemestre and codDisciplina = @codDisciplina and statusMatricula = 'pendente')


	while(@qtdAlunos > 0)
	begin

		set @qtdPresencasAluno = 0
		set @media = 0
		set @frequencia = 0
		
		set @cpf = (select top 1 cpf from #alunos)

		-- Pega a Quantidade de presenças do aluno

		set @qtdPresencasAluno = (select sum(presenca) as qtdPresencasAluno
								  from ListaChamada 
								  where cpf = @cpf 
								  and anoSemestre = @anoSemestre 
								  and codDisciplina = @codDisciplina) 

		--Regra de 3 para descobrir a frequencia

		set @qtdPresencasAluno = (@qtdPresencasAluno * 100)

		set @frequencia = (@qtdPresencasAluno/@qtdPresencasPossiveis)

		insert into #notas (nota)
					select a.nota 
					from Avaliacao a, PesoAvaliacao pav 
					where a.codigoPesoAvaliacao = pav.codigo 
						 and a.cpf = @cpf
						 and a.anoSemestre = @anoSemestre
						 and a.codDisciplina = @codDisciplina
					order by pav.tipo asc

			declare @peso float
	
			DECLARE c CURSOR FOR 
				select peso from PesoAvaliacao where codDisciplina = @codDisciplina order by tipo asc 
			OPEN c
			FETCH NEXT FROM c 
				INTO @peso
			WHILE @@FETCH_STATUS = 0
			BEGIN
			
				set @nota = (select top 1 nota from #notas)


				set @media = (@media + (@nota * @peso))
			
				delete top (1) from #notas
 
				FETCH NEXT FROM c INTO @peso
			END
			CLOSE c
			DEALLOCATE c		
	
		-- Verifica Status de Aprovacao na disciplina
		
		

		if(@frequencia < 75 or @media < 3)
		begin
			set @statusAluno = 'Reprovado'
		end
		else
		begin
			if(@frequencia >= 75 and @media >= 6)
			begin
				set @statusAluno = 'Aprovado'
			end
			else
			begin
				set @statusAluno = 'Exame'
			end
		end

		--Atualiza media e status do aluno na tabela matricula

		update Matricula 
		set nota = @media, statusMatricula = @statusAluno
		where cpf = @cpf
			  and anoSemestre = @anoSemestre
			  and codDisciplina = @codDisciplina

		delete top(1) from #alunos

		set @qtdAlunos = @qtdAlunos - 1
	end

		set @saida = 'Disciplina finalizada com sucesso' 

go

-- trigger que verifica status de frequencia e notas
--   trigger t_calculaMedia_Avaliacao on avaliacao
-- drop trigger t_calculaMedia_Avaliacao
create trigger t_calculaMedia_Avaliacao on Avaliacao
after update
as 
begin
		
	declare @codDisciplina int,
			@cpf char(11),
			@anoSemestre int,
			@nota float,
			@peso float


	set @codDisciplina = (select codDisciplina from deleted)
	set @cpf = (select cpf from deleted)
	set @anoSemestre = (select anoSemestre from deleted)

	--CALCULA MEDIA ATUAL--CALCULA MEDIA ATUAL--CALCULA MEDIA ATUAL--CALCULA MEDIA ATUAL

	declare @qtdPesosAvaliacao int,
			@media decimal(4, 2),
			@statusAluno varchar(20)

		set @media = 0
		set @nota = 0 
		set @peso = 0

	-- Pega a Quantidade de pesos da disciplina

	set @qtdPesosAvaliacao = (select count(codigo) as qtdPesosAvaliacao from PesoAvaliacao where codDisciplina = @codDisciplina)


	create table #pesos(
		peso float
	)

	create table #notas(
		nota float
	)

	-- Insere pesos e notas de forma ordenada nas tabelas temporarias

	insert into #pesos (peso)
		   select peso from PesoAvaliacao where codDisciplina = @codDisciplina order by tipo asc 

	insert into #notas (nota)
			select a.nota 
			from Avaliacao a, PesoAvaliacao pav 
			where a.codigoPesoAvaliacao = pav.codigo 
				 and a.cpf = @cpf
				 and a.anoSemestre = @anoSemestre
				 and a.codDisciplina = @codDisciplina
				  order by pav.tipo asc 

	-- Calcula media da matricula do aluno

	while(@qtdPesosAvaliacao > 0)
	begin
		set @nota = (select top 1 nota from #notas) 
		set @peso = (select top 1 peso from #pesos)

		if(@nota is not null)
		begin
			set @media = (@media + (@nota * @peso))
		end
		
		delete top (1) from #notas
		delete top (1) from #pesos

		set @qtdPesosAvaliacao = @qtdPesosAvaliacao - 1

	end

	-- Verifica Status de Aprovacao na disciplina

	if(@media < 3)
	begin
		set @statusAluno = 'Reprovado'
	end
	else
	begin
		if(@media >= 6)
		begin
			set @statusAluno = 'Aprovado'
		end
		else
		begin
			set @statusAluno = 'Exame'
		end
	end

	--Atualiza media e status do aluno na tabela matricula
		
	update Matricula 
	set nota = @media, statusMatricula = @statusAluno
	where cpf = @cpf
		  and anoSemestre = @anoSemestre
		  and codDisciplina = @codDisciplina

end

go

-- drop function fn_CalculaFaltasEFrequencia
create function fn_CalculaFaltasEFrequencia (@codDisciplina int)
returns @tabela table (
    cpf     char(11),
    nome varchar(150),
    dataChamada date,
	presencaSemana int,
	faltasSemana int,
	totalFaltas int,
    statusAluno varchar(30) null
)
begin
        declare @qtdAulasPorSemana int,
				@qtdChamadas int,
				@horasSemanais varchar(10),
				@horas int,
				@minutos int,
				@totalHoras int,
				@qtdPresencasPossiveis int,
				@frequencia decimal(5,2)

		declare @cpf char(11),
				@nome varchar(150),
				@dataChamada varchar(255),
				@ausencia int,
				@presenca int,
				@totalfaltas int,
				@qtdAlunos int,
				@status varchar(30)

	  -- Pega a quantidade de chamadas realizadas

		set @qtdChamadas = (select count(distinct dataChamada) as numeroDeAulas from ListaChamada where codDisciplina = @codDisciplina)

		-- Pega a Quantidade de presencas possiveis por semana
	
		set @horasSemanais = (select horasSemanais from Disciplina where codDisciplina = @codDisciplina)
		set @horas = (cast(substring(@horasSemanais, 1, charindex(':', @horasSemanais) - 1) as int) * 60 )
		set @minutos = cast(substring(@horasSemanais, charindex(':',@horasSemanais ) + 1, len(@horasSemanais)) as int)
		set @totalHoras = @horas + @minutos

		set @qtdAulasPorSemana = (@totalHoras / 50)

		-- Calcula a Quantidade de presencas possiveis no semestre

		set @qtdPresencasPossiveis = (@qtdChamadas * @qtdAulasPorSemana)

		set @qtdAlunos = (select count(distinct cpf) as qtdAlunos from ListaChamada where codDisciplina = 1)

       DECLARE c CURSOR FOR 
            select m.cpf, a.nome, l.dataChamada, l.ausencia, l.presenca
			from ListaChamada l, Matricula m, Aluno a 
			where a.cpf = m.cpf 
				and m.anoSemestre = l.anoSemestre 
				and m.cpf = l.cpf
				and l.anoSemestre = dbo.fn_obterAnoSemestre()
				and l.codDisciplina = @codDisciplina
				and m.codDisciplina = l.codDisciplina
			order by l.cpf, l.dataChamada asc

       OPEN c
       FETCH NEXT FROM c 
              INTO @cpf, @nome, @dataChamada, @ausencia, @presenca

       WHILE @@FETCH_STATUS = 0
       BEGIN
			
			declare @somaFaltas int

			set @somaFaltas = 0
			set @frequencia = 0

			set @somaFaltas = (select sum(faltasSemana) from @tabela where cpf = @cpf)
			
			if(@somaFaltas is not null)
			begin
				set @totalfaltas = @ausencia + @somaFaltas
			end
			else
			begin 
				set @totalfaltas = @ausencia
			end

			-- Regra de 3 para descobrir a frequência
			SET @frequencia = 100 -((@totalfaltas * 100)/@qtdPresencasPossiveis)

			IF (@frequencia < 75)
			BEGIN
				SET @status = 'Reprovado por Falta';
			END
			ELSE
			BEGIN
				SET @status = 'Aprovado por Presenca';
			END

			insert into @tabela values(@cpf, @nome, @dataChamada, @presenca, @ausencia, @totalfaltas, @status)
 
            FETCH NEXT FROM c INTO @cpf, @nome, @dataChamada, @ausencia, @presenca
       END
       CLOSE c
       DEALLOCATE c

		return
end

go

INSERT INTO Curso (codCurso, nome, cargaHoraria, sigla, notaEnade) 
VALUES 
    (1, 'Análise e Desenvolvimento de Sistemas', 3000, 'ADS', 4),
    (2, 'Medicina', 6000, 'MED', 5),
    (3, 'Administração', 3200, 'ADM', 3),
    (4, 'Ciência da Computação', 3500, 'CCO', 4),
    (5, 'Direito', 3800, 'DIR', 4),
    (6, 'Psicologia', 3400, 'PSI', 3),
    (7, 'Engenharia Elétrica', 4200, 'ELE', 4),
    (8, 'Arquitetura e Urbanismo', 4000, 'ARQ', 4),
    (9, 'Economia', 3000, 'ECO', 3),
    (10, 'Letras', 2800, 'LET', 3);

go

INSERT INTO Professor (codProfessor, nome) 
VALUES 
    (1, 'Prof. João Silva'),
    (2, 'Prof. Maria Oliveira'),
    (3, 'Prof. Carlos Santos'),
    (4, 'Prof. Ana Souza'),
    (5, 'Prof. Pedro Almeida');

go

insert into Disciplina (codCurso, nome, horasSemanais, horaInicio, horaFinal, diaSemana, semestre, codProfessor, turno) values
(1, 'Arquitetura e Organização de Computadores', '03:30', '13:00', '16:30', 'Segunda-feira', 1, 1, 'N'),
(1, 'Laboratório Banco de Dados', '01:40', '14:50', '16:30', 'Terça-feira', 1, 1, 'T'),
(1, 'Metodologia de Pesquisa Científico Tecnológica', '01:40', '14:50', '16:30', 'Quarta-feira', 1, 2, 'T')

go

insert into PesoAvaliacao (codDisciplina, tipo, peso) values
(1, 'P1', 0.3),
(1, 'P2', 0.5),
(1, 'T', 0.2),
(2, 'P1', 0.33),
(2, 'P2', 0.33),
(2, 'P3', 0.33),
(3, 'Artigo Resumido', 0.3),
(3, 'Monografia', 0.5)

go

