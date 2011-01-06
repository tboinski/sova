praca.pdf:	*.tex *.bib */*
	@echo Kompilowanie praca.pdf
	pdflatex praca.tex && bibtex praca && pdflatex praca.tex && pdflatex praca.tex
.PHONY: clean
clean:
	$(RM) *.log *.toc *.pdf *.aux *.bbl *.blg *.rtf *.brf
.PHONY: lp
lp:	praca.pdf
	evince praca.pdf &
.PHONY: mp
mp: praca.pdf
	open praca.pdf
.PHONY: rtf
rtf: praca.pdf
	latex2rtf praca.tex
.PHONY: mrtf
mrtf: rtf
	open praca.rtf
