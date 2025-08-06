//
//  RssRow.swift
//  iosApp
//
//  Created by Ekaterina.Petrova on 25.10.2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import RssReader
import URLImage

struct PostRow: View {
    let post: Item
    
    var body: some View {
        if let postURL = post.link, let url = URL(string: postURL) {
            Link(destination: url) {
                content
            }
            .foregroundColor(.black)
        } else {
            content
        }
    }
    
    var content: some View {
        VStack(alignment: .leading, spacing: 10.0) {
            if let title = post.title {
                Text(title).bold().font(.title3)
            }
            if let imageUrl = post.mediaContent?.url, let url = URL(string: imageUrl) {
                URLImage(url: url) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                }
                .frame(minWidth: 0, maxWidth: .infinity)
                .clipped()
            }
            if let descr = post.description_ {
                Text(descr).font(.body).lineLimit(5)
            }
            HStack{
                Spacer()
                if let dateString = post.pubDate {
                    Text(Item.dateFormatter.string(from: Date(from: dateString))).font(.footnote).foregroundColor(.gray)
                }
            }
        }
    }
}

extension Item {
    static let dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "E, MMM d HH:mm"
        return formatter
    }()
    
    var linkURL: URL? {
        if let link = link {
            return URL(string: link)
        } else {
            return nil
        }
    }
}

