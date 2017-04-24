# Copy the installation section to a separate file.
echo -n 'Extracting INSTALL.txt from README.txt...'
perl -ne 'if (/^# == INSTALLATION/../^# == [^I]/) { print unless /^# == [^I]/; }' README.txt > ! INSTALL.txt
echo 'done.'
# Copy the license section to a separate file.
echo -n 'Extracting LICENSE.txt from README.txt...'
perl -ne 'if (/^# == LICENSE ==/../^# == [^LR]/) { print unless /^# == [^LR]/; }' README.txt > ! LICENSE.txt
echo 'done.'
# Copy manual to release directory.
echo -n 'Copying manual to release directory...'
/bin/cp -p doc/src/manual.pdf doc
echo 'done.'
