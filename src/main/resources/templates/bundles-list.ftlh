<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Bundles</title>

	<style>

        body {
            background-color: black;
            color: chartreuse;
            font-family: monospace;
            font-size: 1rem;
        }

        a:visited {
            color: aqua;
        }

        a {
            color: chartreuse;
        }

        .expires-in {
            font-style: italic;
        }

        nav ol {
            list-style-type: none;
        }

        nav li {
            display: inline-block;
            padding: 0 0.5rem;
            min-width: 8rem;
        }

        table.bundles {
	        width: 100%;
        }

        table.bundles thead {
	        font-size: 1.2rem;
	        text-align: left;
        }

        table.bundles tbody td {
	        border-top: 1px dotted #537f38;
        }
	</style>
</head>
<body>

<nav>
	<ol>
		<li><a href="/bundles">All Bundle Types</a></li>
		<li><a href="/bundles/music">Music</a></li>
		<li><a href="/bundles/games">Games</a></li>
		<li><a href="/bundles/ebooks">Ebooks</a></li>
		<li><a href="/bundles/comics">Comics</a></li>
	</ol>
</nav>

<table class="bundles">
	<thead>
	<tr>
		<th>Title</th>
		<th>Details</th>
		<th>Ends On</th>
		<th>&nbsp;</th>
	</tr>
	</thead>

	<tbody>
    <#list bundles as bundle>
		<tr>
			<td>
				<a href="${bundle.url}" target="_blank">
                    ${bundle.title}
				</a>
			</td>
			<td>
                ${bundle.details}
                <#list bundle.products as product>
					<span>${product.icon?no_esc}</span>
                </#list>
			</td>
			<td>${bundle.endsOn}
				<span class="expires-in">(${bundle.expiresIn} days)</span>
			</td>
		</tr>
    </#list>
	</tbody>
</table>

</body>
</html>
