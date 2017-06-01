/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.impl.index.schema;

import org.neo4j.index.internal.gbptree.Layout;

/**
 * {@link Layout} for numbers where numbers need to be unique.
 */
class UniqueNumberLayout extends NumberLayout
{
    private static final String IDENTIFIER_NAME = "UNI";

    @Override
    public long identifier()
    {
        // todo Is Number.Value.SIZE a good checksum?
        return Layout.namedIdentifier( IDENTIFIER_NAME, NumberValue.SIZE );
    }

    @Override
    public int compare( NumberKey o1, NumberKey o2 )
    {
        int comparison = Double.compare( o1.value, o2.value );
        if ( comparison == 0 )
        {
            // This is a special case where we need to check if o2 is the unbounded max key.
            // This needs to be checked to be able to support storing Double.MAX_VALUE and retrieve it later.
            if ( o2.isHighest )
            {
                return -1;
            }
            if ( o1.isHighest )
            {
                return 1;
            }
        }
        return comparison;
    }
}
