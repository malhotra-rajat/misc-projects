using System;
using System.Collections.Generic;
using System.Text;

using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Audio;

namespace CreatingA2DSprite
{
    class Sprite
    {
        //The current position of the Sprite

        //The texture object used when drawing the sprite

       // public string assestname;







        //Load the texture for the sprite using the Content Pipeline
        public void LoadContent(ContentManager theContentManager, string theAssetName)
        {

        }
        public void Update(GameTime theGameTime, Vector2 theSpeed, Vector2 theDirection)
        {





        }
        //Draw the sprite to the screen
        public virtual void Draw(SpriteBatch theSpriteBatch)
        {

        }

    }
}
