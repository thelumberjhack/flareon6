class FlareBear:

    def __init__(self, mass=0, happy=0, clean=0):
        self.mass = mass
        self.happy = happy
        self.clean = clean
        self.feeds: int = 0
        self.plays: int = 0
        self.cleans: int = 0
        self.poos: float = 0.0
        self.ecstatic = False
        self.activities: str = ""

    def do_feed(self):
        self.mass += 10
        self.happy += 2
        self.clean -=1
        self.poos += 0.34
        self.feeds += 1
        self.ecstatic = self.isEcstatic()

    def do_play(self):
        self.mass -= 2
        self.happy += 4
        self.clean -= 1
        self.plays += 1
        self.ecstatic = self.isEcstatic()
    
    def do_clean(self):
        self.happy -= 1
        self.clean += 6
        self.poos -= 1.0
        self.cleans += 1
        self.ecstatic = self.isEcstatic()

    def __str__(self):
        return f"Bear:\nmass: {self.mass}\nhappy: {self.happy}\nclean: {self.clean}\npoos: {self.poos}"
    
    def isEcstatic(self):
        return self.mass == 72 and self.happy == 30 and self.clean ==0

    def isHappy(self):
        stat:float = self.feeds/self.plays
        return stat >=2.0 and stat <=2.5

def solve():
    bear = FlareBear()
    for _ in range(8):
        bear.do_feed()

    for _ in range(4):
        bear.do_play()

    for _ in range(2):
        bear.do_clean()

    if bear.isHappy() and bear.isEcstatic():
        print(bear)
        print(f"feeds: {bear.feeds}\nplays: {bear.plays}\ncleans: {bear.cleans}")

if __name__ == "__main__":
    solve()
