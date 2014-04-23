using System.Runtime.InteropServices;
using System.Text;

namespace System.INI
{
    public class INIFile
    {
        private readonly string _path;

        [DllImport ("kernel32")]
        private static extern long WritePrivateProfileString (string section, string key, string val, string filePath);
        [DllImport ("kernel32")]
        private static extern int GetPrivateProfileString (string section, string key, string def, StringBuilder retVal, int size, string path);

        public INIFile (string filePath)
        {
            _path = filePath;
        }

        public void SetValue (string section, string key, string value)
        {
            WritePrivateProfileString (section, key, value, _path);
        }

        public string GetValue (string section, string key)
        {
            var temp = new StringBuilder (255);
            GetPrivateProfileString (section, key, "", temp, 255, _path);
            if (temp.ToString() == "") throw new Exception("Key has empty Value");
            return temp.ToString ();
        }
    }
}
