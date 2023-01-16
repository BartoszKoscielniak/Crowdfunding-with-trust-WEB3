const Background = () => {
    return (
        <div className="bg-zinc-800 bg-cover object-contain w-full h-full relative">
            <div className="absolute w-[250px] h-[250px] top-1/4 left-1/4 animate-rotation">
                <div className="absolute bg-violet-900 w-[650px] h-[650px] top-[-10px] left-[-10px] rounded-full blur-[100px] animate-flashGradient" />
            </div>

            <div className="absolute w-[250px] h-[250px] top-[55%] left-[55%] animate-rotationv2">
                <div className="absolute bg-green-900 w-[650px] h-[650px] top-[-10px] left-[-10px] rounded-full blur-[100px] animate-flashGradientv2" />
            </div>
        </div>
    );
}

export default Background;