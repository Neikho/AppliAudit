#-------------------------------------------------------------------------------
# usage : perl compile.pl
#-------------------------------------------------------------------------------
use strict;
use warnings;

#Importe dans ce répertoire les sources à jour.
print("Cleaning...");
system "rm ./*.class outputs/auditOutput.html outputs/auditResult.xml outputs/definitiveHtml.html outputs/toto.pdf";
print("\nCompiling java src...");
system "javac /home/oracle/audit_auto/version3/src/*/*.java";
print("\nCompiling main...");
system "javac /home/oracle/audit_auto/version3/AuditMain.java";
print("\nExecuting main...");
system "java AuditMain";
print("\nConverting html file to pdf...");
system "wkhtmltopdf ./outputs/definitiveHtml.html ./outputs/toto.pdf";
print("\nDone.\n");
