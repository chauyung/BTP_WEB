if [ "$1" = "" ] || [ "$2" = "" ] 
then
   echo " arg not enoughth should be include src and target dolder."  
   exit 1
fi
echo change folder : $1
cd $1
pwd
ls -la
cd btp_ui
pwd
ls -la
tar -xvf ./node_modules.tar.gz -C $2
chmod +x node_modules/.bin/*
ls -la
