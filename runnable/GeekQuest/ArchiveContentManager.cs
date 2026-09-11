// Compatibility loader for the archived PNG/WAV assets; no XNA build tools required.
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace CreatingA2DSprite
{
    public sealed class ArchiveContentManager : ContentManager
    {
        private readonly Dictionary<string, object> cache = new Dictionary<string, object>();
        private readonly string contentPath;
        private readonly string fontPath;
        public ArchiveContentManager(IServiceProvider services) : base(services)
        {
            contentPath = Environment.GetEnvironmentVariable("GEEK_QUEST_CONTENT")
                ?? throw new InvalidOperationException("Launch with tools/run_geek_quest.py to locate original assets.");
            fontPath = Environment.GetEnvironmentVariable("GEEK_QUEST_FONTS")
                ?? throw new InvalidOperationException("Generated font directory is missing.");
        }
        private GraphicsDevice Device => ((IGraphicsDeviceService)ServiceProvider.GetService(typeof(IGraphicsDeviceService))).GraphicsDevice;
        private static string Resolve(string root, string name)
        {
            string current = root;
            foreach (string part in name.Replace('\\', '/').Split('/'))
            {
                if (part == "..") throw new ContentLoadException("Parent asset paths are not supported.");
                current = Directory.EnumerateFileSystemEntries(current)
                    .FirstOrDefault(p => Path.GetFileName(p).Equals(part, StringComparison.OrdinalIgnoreCase))
                    ?? throw new ContentLoadException("Asset not found: " + name);
            }
            return current;
        }
        private Texture2D Texture(string path, bool colorKey)
        {
            using var stream = File.OpenRead(path);
            var texture = Texture2D.FromStream(Device, stream);
            var pixels = new Color[texture.Width * texture.Height];
            texture.GetData(pixels);
            for (int i = 0; i < pixels.Length; i++)
            {
                var c = pixels[i];
                pixels[i] = colorKey && c.R == 255 && c.G == 0 && c.B == 255
                    ? Color.Transparent : Color.FromNonPremultiplied(c.R, c.G, c.B, c.A);
            }
            texture.SetData(pixels);
            return texture;
        }
        public override T Load<T>(string assetName)
        {
            string key = typeof(T).FullName + ":" + assetName.ToLowerInvariant();
            if (cache.TryGetValue(key, out object found)) return (T)found;
            object result;
            if (typeof(T) == typeof(Texture2D))
                result = Texture(Resolve(contentPath, assetName + ".png"), true);
            else if (typeof(T) == typeof(SoundEffect))
            {
                using var stream = File.OpenRead(Resolve(contentPath, assetName + ".wav"));
                result = SoundEffect.FromStream(stream);
            }
            else if (typeof(T) == typeof(SpriteFont))
            {
                using var doc = JsonDocument.Parse(File.ReadAllText(Path.Combine(fontPath, assetName + ".json")));
                var data = doc.RootElement;
                var glyphs = new List<Rectangle>(); var crops = new List<Rectangle>();
                var chars = new List<char>(); var kern = new List<Vector3>();
                foreach (var g in data.GetProperty("glyphs").EnumerateArray())
                {
                    int w = g.GetProperty("width").GetInt32(), h = g.GetProperty("height").GetInt32();
                    glyphs.Add(new Rectangle(g.GetProperty("x").GetInt32(), g.GetProperty("y").GetInt32(), w, h));
                    crops.Add(new Rectangle(0, 0, w, h));
                    chars.Add((char)g.GetProperty("code").GetInt32());
                    kern.Add(new Vector3(0, g.GetProperty("advance").GetSingle(), 0));
                }
                result = new SpriteFont(Texture(Path.Combine(fontPath, assetName + ".png"), false),
                    glyphs, crops, chars, data.GetProperty("lineHeight").GetInt32(),
                    data.GetProperty("spacing").GetSingle(), kern, '?');
            }
            else throw new ContentLoadException("Unsupported archived asset type: " + typeof(T));
            cache.Add(key, result);
            return (T)result;
        }
        public override void Unload()
        {
            foreach (object value in cache.Values)
                if (value is IDisposable disposable) disposable.Dispose();
            cache.Clear();
            base.Unload();
        }
    }
}
