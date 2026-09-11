using System;

namespace CreatingA2DSprite
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        static void Main(string[] args)
        {
            Console.WriteLine("Geek Quest: loading original assets on MonoGame DesktopGL.");
            using (Game1 game = new Game1())
            {
                game.Run();
            }
        }
    }
}
