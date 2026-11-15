from pathlib import Path
root=Path(__file__).resolve().parents[1]
s=(root/'dist/index.html').read_text()
s=s.replace('<link rel="stylesheet" href="style.css">','<style>'+(root/'dist/style.css').read_text()+'</style>')
for name in ['engine.js','provider.js','app.js']:
 s=s.replace(f'<script src="{name}"></script>','<script>'+(root/'dist'/name).read_text()+'</script>')
(root/'dist/PartSaver.html').write_text(s)
print('Built dist/PartSaver.html')
