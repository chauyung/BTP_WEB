
echo "current run time java version"
java -version

PATH=/u01/nodejs-linux/node-v20.16.0-linux-x64/bin:$PATH; export PATH

echo $PATH
npm -v
node -v

# LD_LIBRARY_PATH �����A�Τ��n�b���� node �ɸ��J���ӥؿ�
# �����ܼơ]LD_LIBRARY_PATH�^���V�F�@�Ӥ����T�� libcrypto.so.1.1
# unset LD_LIBRARY_PATH

if [ "$1" = "" ] || [ "$2" = "" ] 
then
   echo " arg not enoughth should be include src and target dolder."  
   exit 1
fi
echo change folder : $1
cd $1
pwd
npm -v
node -v

npm run build
echo change folder : $2
cd $2
pwd


