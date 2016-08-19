using System;
using System.Runtime.Serialization;

namespace TouClick
{
    [Serializable]
    internal class TouclickException : Exception
    {
        public TouclickException()
        {
        }

        public TouclickException(string message) : base(message)
        {
        }

        public TouclickException(string message, Exception innerException) : base(message, innerException)
        {
        }

        protected TouclickException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}