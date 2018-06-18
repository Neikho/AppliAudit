#-------------------------------------------------------------------------------
# usage : perl compile.pl
#-------------------------------------------------------------------------------
use strict;
use warnings;

#Importe dans ce répertoire les sources à jour.
print("Cleaning...");
system "rm ./*.class src/*/*.class outputs/auditResult.xml outputs/*.pdf";
print("\nCompiling java src...");
system "javac src/*/*.java";
print("\nCompiling main...");
system "javac AuditMain.java";
print("\nExecuting main...");
system "java AuditMain";
print("\nConverting html file to pdf...");
system "wkhtmltopdf ./outputs/htmlFinal.html --header-html outputs/headFoot/header.html --footer-html outputs/headFoot/footer.html --footer-right '[page] sur [toPage]' --footer-font-size 8 ./outputs/sw.pdf";
print("\nDone.\n");
