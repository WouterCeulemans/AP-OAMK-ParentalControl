using System.Runtime.InteropServices;
using System.Text;

namespace System.INI
{
    public class INIFile
    {
        public string Path;

        [DllImport ("kernel32")]
        private static extern long WritePrivateProfileString (string section, string key, string val, string filePath);
        [DllImport ("kernel32")]
        private static extern int GetPrivateProfileString (string section, string key, string def, StringBuilder retVal, int size, string path);

        public INIFile (string filePath)
        {
            Path = filePath;
        }

        public void SetValue (string section, string key, string value)
        {
            WritePrivateProfileString (section, key, value, Path);
        }

        public string GetValue (string section, string key)
        {
            var temp = new StringBuilder (255);
            GetPrivateProfileString (section, key, "", temp, 255, Path);
            return temp.ToString ();
        }
    }
}
