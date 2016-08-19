using System;
using System.Collections.Generic;

using System.Text;


namespace Touclick
{
    public class Parameter
    {
        public string b { get; set; }
        public string v { get; set; }

      
        public Parameter(string name, string value)
        {
            this.name = name;
            this.value = value;
        }
        public string name { get; set; }
        public string value { get; set; }
    }
}

