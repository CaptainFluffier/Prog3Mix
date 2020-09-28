Prižig programa je enostaven in podoben kot na vajah.
Program vzame 2 argumenta in sicer 1. številko porta ter 2. ip trusted serverja
Prvi port mora nujno biti 5000, saj je nastavljen kot trusted ip je vseen
pri ostalih zagonih so porti lahko poljubni (še ne uporabljeni), drugi argument je vedno isti in sicer naš ip (izpiše ga konzola po zagonu porta 5000).
html spletne strani zahtevate prek Scannerja oz. buffered reader v programu, preprosto napišete url in pritisnete enter, številka na koncu je čas (v ms), ki ga je program potreboval.

Dockerfile in java jar se nahajata v mapi in sicer Dchat\out\artifacts in Dchat\out\artifacts\Dchat_jar. Postopek po katerem sem zagnal docker:
1. v cmd sem se pomaknil v mapo Dchat\out\artifacts.
2. docker build .
3. docker run -i -t <imagename> <portn> <trusted ip>